package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.proxy.core.config.ProxyCoreConfig;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.RandomUtil;
import de.benjaminborbe.tools.util.ThreadRunner;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Singleton
public class ProxySocket {

	private static final String NEWLINE = "\r\n";

	private final Logger logger;

	private final ProxyRequestHandler proxyRequestHandler;

	private final DurationUtil durationUtil;

	private final ParseUtil parseUtil;

	private final ProxyLineReader proxyLineReader;

	private final ProxyLineParser proxyLineParser;

	private final ThreadRunner threadRunner;

	private final StreamUtil streamUtil;

	private final RandomUtil randomUtil;

	private final IdGeneratorUUID idGenerator;

	private final ProxyConversationNotifier proxyConversationNotifier;

	private final ProxyCoreConfig proxyCoreConfig;

	private ServerSocket serverSocket = null;

	@Inject
	public ProxySocket(
		final Logger logger,
		final ProxyRequestHandler proxyRequestHandler,
		final DurationUtil durationUtil,
		final ParseUtil parseUtil,
		final ProxyLineReader proxyLineReader,
		final ProxyLineParser proxyLineParser,
		final ThreadRunner threadRunner,
		final StreamUtil streamUtil,
		final RandomUtil randomUtil,
		final IdGeneratorUUID idGenerator,
		final ProxyConversationNotifier proxyConversationNotifier,
		final ProxyCoreConfig proxyCoreConfig
	) {
		this.logger = logger;
		this.proxyRequestHandler = proxyRequestHandler;
		this.durationUtil = durationUtil;
		this.parseUtil = parseUtil;
		this.proxyLineReader = proxyLineReader;
		this.proxyLineParser = proxyLineParser;
		this.threadRunner = threadRunner;
		this.streamUtil = streamUtil;
		this.randomUtil = randomUtil;
		this.idGenerator = idGenerator;
		this.proxyConversationNotifier = proxyConversationNotifier;
		this.proxyCoreConfig = proxyCoreConfig;
	}

	public synchronized void start() {
		if (serverSocket == null) {
			logger.info("start");
			threadRunner.run("proxy", new ProxyRunnable());
		} else {
			logger.info("start failed, already running");
		}
	}

	public void stop() {
		if (serverSocket != null) {
			try {
				logger.info("stop");
				serverSocket.close();
			} catch (IOException e) {
				// nop
			} finally {
				serverSocket = null;
			}
		} else {
			logger.info("stop failed, not running");
		}
	}

	private int createRandomPort() {
		return randomUtil.getRandomInt(1024, 0xFFFF);
	}

	private class ProxyRunnable implements Runnable {

		@Override
		public void run() {
			try {
				final int port = getPort();
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
			} catch (IOException e) {
				logger.info(e.getClass().getName(), e);
			} finally {
				if (serverSocket != null)
					try {
						serverSocket.close();
						serverSocket = null;
					} catch (IOException e) {
						// nop
					}
			}
		}

	}

	private int getPort() {
		final int port;
		if (proxyCoreConfig.randomPort()) {
			port = createRandomPort();
		} else {
			port = proxyCoreConfig.getPort();
		}
		return port;
	}
}
