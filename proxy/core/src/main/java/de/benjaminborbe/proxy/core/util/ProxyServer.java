package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.proxy.core.config.ProxyCoreConfig;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.util.RandomUtil;
import de.benjaminborbe.tools.util.ThreadRunner;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Singleton
public class ProxyServer {

	private final Logger logger;

	private final ProxyRequestHandler proxyRequestHandler;

	private final ThreadRunner threadRunner;

	private final RandomUtil randomUtil;

	private final ProxyCoreConfig proxyCoreConfig;

	private ServerSocket serverSocket = null;

	@Inject
	public ProxyServer(
		final Logger logger,
		final ProxyRequestHandler proxyRequestHandler,
		final ThreadRunner threadRunner,
		final RandomUtil randomUtil,
		final IdGeneratorUUID idGenerator,
		final ProxyCoreConfig proxyCoreConfig
	) {
		this.logger = logger;
		this.proxyRequestHandler = proxyRequestHandler;
		this.threadRunner = threadRunner;
		this.randomUtil = randomUtil;
		this.proxyCoreConfig = proxyCoreConfig;
	}

	public Integer getPort() {
		if (serverSocket != null) {
			return serverSocket.getLocalPort();
		}
		return null;
	}

	public synchronized void start() {
		if (serverSocket == null) {
			logger.info("start");
			final int port = createPort();
			threadRunner.run("proxy", new ProxyRunnable(port));
		} else {
			logger.info("start failed, already running");
		}
	}

	public void stop() {
		if (serverSocket != null) {
			try {
				logger.info("stop");
				serverSocket.close();
			} catch (final IOException e) {
				// nop
			} finally {
				serverSocket = null;
			}
		} else {
			logger.info("stop failed, not running");
		}
	}

	private class ProxyRunnable implements Runnable {

		private final int port;

		public ProxyRunnable(final int port) {
			this.port = port;
		}

		@Override
		public void run() {
			try {
				logger.info("create ServerSocket on port: " + port);
				serverSocket = new ServerSocket(port);
				serverSocket.setReuseAddress(false);
				// serverSocket.setSoTimeout(4000);
				// serverSocket.bind(new InetSocketAddress("0.0.0.0", PORT));
				// serverSocket.bind(new InetSocketAddress("127.0.0.1", PORT));
				while (serverSocket != null) {
					final Socket clientSocket = serverSocket.accept();
					threadRunner.run("request", new Runnable() {

						@Override
						public void run() {
							proxyRequestHandler.handleRequest(clientSocket);
						}
					});
				}
			} catch (final IOException e) {
				logger.info(e.getClass().getName(), e);
			} finally {
				if (serverSocket != null)
					try {
						serverSocket.close();
						serverSocket = null;
					} catch (final IOException e) {
						// nop
					}
			}
		}
	}

	private int createRandomPort() {
		return randomUtil.getRandomInt(1024, 0xFFFF);
	}

	private int createPort() {
		final int port;
		if (proxyCoreConfig.randomPort()) {
			port = createRandomPort();
		} else {
			port = proxyCoreConfig.getPort();
		}
		return port;
	}
}
