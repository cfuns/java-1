package de.benjaminborbe.loggly.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.loggly.config.LogglyConfig;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Singleton
public class LogglyConnectorImpl implements LogglyConnector {

	private final class CloseRunnable implements Runnable {

		@Override
		public void run() {
			close();
		}
	}

	private final class RetryRunnable implements Runnable {

		@Override
		public void run() {
			while (allowRetry) {
				// drain the retry requests
				LogglyEntry sample = null;
				while ((sample = retryQueue.poll()) != null) {
					if (sample.getRetryCount() > 10) {
						// todo: capture statistics about the failure (exception and/or status code)
						// and then report on it in some sort of thoughtful way to standard err
					} else {
						pool.submit(sample);
					}
				}

				// retry every 10 seconds
				try {
					Thread.sleep(10000);
				} catch (final InterruptedException e) {
					System.err.println("Retry sleep was interrupted, giving up on retry thread");
					return;
				}
			}
		}
	}

	private class LogglyEntry implements Runnable {

		private int retryCount;

		private final Map<String, String> data;

		public LogglyEntry(final Map<String, String> data) {
			this.data = data;
		}

		private String buildJsonString() throws IOException {
			final StringWriter sw = new StringWriter();
			final JSONObject object = new JSONObjectSimple();
			object.putAll(data);
			object.writeJSONString(sw);
			return sw.toString();
		}

		public int getRetryCount() {
			return retryCount;
		}

		public void increaseRetryCount() {
			retryCount++;
		}

		@Override
		public void run() {
			HttpEntity entity = null;
			try {
				final HttpPost post = new HttpPost(logglyConfig.getInputUrl());

				post.setEntity(new StringEntity(buildJsonString()));
				post.setHeader("Content-Type", "application/x-www-form-urlencoded");
				final HttpResponse response = httpClient.execute(post);
				entity = response.getEntity();
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					if (allowRetry) {
						increaseRetryCount();
						retryQueue.offer(this);
					}
				} else {
					logger.debug("send loggy");
				}
			} catch (final Exception e) {
				if (allowRetry) {
					increaseRetryCount();
					retryQueue.offer(this);
				}
			} finally {
				if (entity != null) {
					try {
						entity.consumeContent();
					} catch (final IOException e) {
						// safe to ignore
					}
				}
			}
		}
	}

	private boolean allowRetry = true;

	private DefaultHttpClient httpClient;

	private final LogglyConfig logglyConfig;

	private ThreadPoolExecutor pool;

	private LinkedBlockingQueue<LogglyEntry> retryQueue;

	private final Logger logger;

	@Inject
	public LogglyConnectorImpl(final Logger logger, final LogglyConfig logglyConfig) {
		this.logger = logger;
		this.logglyConfig = logglyConfig;

		init();
	}

	@Override
	public synchronized void close() throws SecurityException {
		if (pool.isShutdown()) {
			return;
		}

		try {
			// first, anything in the retry queue should be tried one last time and then we give
			// up on it
			allowRetry = false;
			for (final LogglyEntry sample : retryQueue) {
				pool.submit(sample);
			}
			retryQueue.clear();

			System.out.println("Shutting down Loggly handler - waiting 90 seconds for " + pool.getQueue().size() + " logs to finish");
			pool.shutdown();
			try {
				final boolean result = pool.awaitTermination(90, TimeUnit.SECONDS);
				if (!result) {
					System.out.println("Not all Loggly messages sent out - still had " + pool.getQueue().size() + " left :(");
					pool.shutdownNow();
				}
			} catch (final InterruptedException e) {
				// ignore
			}
		} finally {
			httpClient.getConnectionManager().shutdown();
			System.out.println("Loggly handler shut down");
		}
	}

	@Override
	public void debug(final String message) {
		log(LogLevel.DEBUG, message);
	}

	@Override
	public void debug(final String message, final Map<String, String> data) {
		log(LogLevel.DEBUG, message, data);
	}

	@Override
	public void error(final String message) {
		log(LogLevel.ERROR, message);
	}

	@Override
	public void error(final String message, final Map<String, String> data) {
		log(LogLevel.ERROR, message, data);
	}

	@Override
	public void fatal(final String message) {
		log(LogLevel.FATAL, message);
	}

	@Override
	public void fatal(final String message, final Map<String, String> data) {
		log(LogLevel.FATAL, message, data);
	}

	@Override
	public void info(final String message) {
		log(LogLevel.INFO, message);
	}

	@Override
	public void info(final String message, final Map<String, String> data) {
		log(LogLevel.INFO, message, data);
	}

	private void init() {

		pool = new ThreadPoolExecutor(logglyConfig.getMaxThreads(), logglyConfig.getMaxThreads(), 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(logglyConfig.getBacklog()),
			new ThreadFactory() {

				@Override
				public Thread newThread(final Runnable r) {
					final Thread thread = new Thread(r, "Loggly Thread");
					thread.setDaemon(true);
					return thread;
				}
			}, new ThreadPoolExecutor.DiscardOldestPolicy());
		pool.allowCoreThreadTimeOut(true);

		retryQueue = new LinkedBlockingQueue<>(logglyConfig.getBacklog());

		final Thread retryThread = new Thread(new RetryRunnable(), "Loggly Retry Thread");
		retryThread.setDaemon(true);
		retryThread.start();

		final HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, logglyConfig.getMaxThreads());
		final ConnPerRouteBean connPerRoute = new ConnPerRouteBean(logglyConfig.getMaxThreads());
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

		// set 15 second timeouts, since Loggly should return quickly
		params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
		params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);

		final SchemeRegistry registry = new SchemeRegistry();
		try {
			registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		} catch (final Exception e) {
			throw new RuntimeException("Could not register SSL socket factor for Loggly", e);
		}

		final ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(params, registry);

		httpClient = new DefaultHttpClient(connManager, params);

		// because the threads a daemon threads, we want to give them a chance
		// to finish up before we totally shut down
		Runtime.getRuntime().addShutdownHook(new Thread(new CloseRunnable()));
	}

	@Override
	public void log(final LogLevel logLevel, final String message) {
		log(logLevel, message, new HashMap<String, String>());
	}

	@Override
	public void log(final LogLevel logLevel, final String message, final Map<String, String> data) {
		data.put("thread", Thread.currentThread().getName());
		data.put("level", String.valueOf(logLevel));
		data.put("message", message);
		pool.submit(new LogglyEntry(data));
	}

	@Override
	public void trace(final String message) {
		log(LogLevel.TRACE, message);
	}

	@Override
	public void trace(final String message, final Map<String, String> data) {
		log(LogLevel.TRACE, message, data);
	}

	@Override
	public void warn(final String message) {
		log(LogLevel.WARN, message);
	}

	@Override
	public void warn(final String message, final Map<String, String> data) {
		log(LogLevel.WARN, message, data);
	}
}
