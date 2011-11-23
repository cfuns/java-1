package de.benjaminborbe.index.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.util.HttpDownloadResult;
import de.benjaminborbe.tools.util.HttpDownloader;
import de.benjaminborbe.tools.util.ThreadRunner;

@Singleton
public class TwentyFeetPerformanceServiceImpl implements TwentyFeetPerformanceService {

	private final class DownloadRunnable implements Runnable {

		// 5 sec timeout
		private static final int TIMEOUT = 5 * 1000;

		private final URL url;

		private final Map<URL, HttpDownloadResult> result;

		private DownloadRunnable(final URL url, final Map<URL, HttpDownloadResult> result) {
			this.url = url;
			this.result = result;
		}

		@Override
		public void run() {
			try {
				result.put(url, httpDownloader.downloadUrlUnsecure(url, TIMEOUT));
			}
			catch (final IOException e) {
				logger.debug("IOException", e);
				result.put(url, null);
			}
		}
	}

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final ThreadRunner threadRunner;

	@Inject
	public TwentyFeetPerformanceServiceImpl(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final ThreadRunner threadRunner) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.threadRunner = threadRunner;
	}

	@Override
	public Map<URL, HttpDownloadResult> getPerformance() throws IOException {
		logger.debug("getPerformance");
		final Map<URL, HttpDownloadResult> result = new HashMap<URL, HttpDownloadResult>();
		final Set<Thread> threads = new HashSet<Thread>();
		for (final URL url : getURLs()) {
			threads.add(threadRunner.run("urlDownload", new DownloadRunnable(url, result)));
		}
		for (final Thread thread : threads) {
			try {
				thread.join();
			}
			catch (final InterruptedException e) {
			}
		}
		return result;
	}

	protected Collection<URL> getURLs() throws MalformedURLException {
		final Set<URL> urls = new HashSet<URL>();

		// extern urls
		{
			final List<String> hosts = Arrays.asList("http://www.heise.de/index.html", "http://aws.amazon.com/",
					"https://aws.amazon.com/");
			for (final String host : hosts) {
				urls.add(new URL(host));
			}
		}

		// twentyfeet urls
		{
			final List<String> hosts = Arrays.asList("https://www.twentyfeet.com", "https://frontend1.twentyfeet.com",
					"https://frontend2.twentyfeet.com", "http://www.twentyfeet.com", "http://frontend1.twentyfeet.com",
					"http://frontend2.twentyfeet.com", "http://test.twentyfeet.com", "https://test.twentyfeet.com");
			final List<String> parts = Arrays.asList("/", "/app", "/iframe-website/de/index.html", "/wiki/dashboard.action");
			for (final String host : hosts) {
				for (final String part : parts) {
					urls.add(new URL(host + part));
				}
			}
		}
		return urls;
	}
}
