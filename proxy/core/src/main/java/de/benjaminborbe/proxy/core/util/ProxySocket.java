package de.benjaminborbe.proxy.core.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.StreamUtil;
import de.benjaminborbe.tools.util.ThreadRunner;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Singleton
public class ProxySocket {

	private static final int PORT = 9999;

	private static final String NEWLINE = "\r\n";

	private static final String PROXY_NAME = "BBProxy";

	private final Logger logger;

	private final ProxyUtil proxyUtil;

	private final ThreadRunner threadRunner;

	private final StreamUtil streamUtil;

	private ServerSocket serverSocket = null;

	@Inject
	public ProxySocket(final Logger logger, final ProxyUtil proxyUtil, final ThreadRunner threadRunner, final StreamUtil streamUtil) {
		this.logger = logger;
		this.proxyUtil = proxyUtil;
		this.threadRunner = threadRunner;
		this.streamUtil = streamUtil;
	}

	private String readLine(final InputStream is) throws IOException {

		StringBuffer line = new StringBuffer();
		int i;
		char c = 0x00;
		i = is.read();
		if (i == -1)
			return null;
		while (i > -1 && i != 10 && i != 13) {
			// Convert the int to a char
			c = (char) (i & 0xFF);
			line = line.append(c);
			i = is.read();
		}
		if (i == 13) { // 10 is unix LF, but DOS does 13+10, so read the 10 if we got 13
			i = is.read();
		}

		return line.toString();
	}

	public void start() {
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
			}
			serverSocket = null;
		} else {
			logger.info("stop failed, not running");
		}
	}

	private String readHeader(final InputStream is) throws IOException {
		final StringWriter stringWriter = new StringWriter();
		String line;
		do {
			line = readLine(is);
			stringWriter.append(line);
			stringWriter.append(NEWLINE);
		} while (line != null && line.length() > 1);
		if (line != null) {
			stringWriter.append(line);
			stringWriter.append(NEWLINE);
		}
		return stringWriter.toString();
	}

	private class ProxyRunnable implements Runnable {

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(PORT);
				serverSocket.setReuseAddress(false);
				// serverSocket.setSoTimeout(4000);
				// serverSocket.bind(new InetSocketAddress("0.0.0.0", PORT));
				while (serverSocket != null) {
					final Socket clientSocket = serverSocket.accept();
					threadRunner.run("request", new ProxyRequestRunnable(clientSocket));
				}
			} catch (IOException e) {
				logger.info(e.getClass().getName(), e);
			} finally {
				if (serverSocket != null)
					try {
						serverSocket.close();
						serverSocket = null;
					} catch (IOException e) {

					}
			}
		}

		private class ProxyRequestRunnable implements Runnable {

			private final Socket clientSocket;

			public ProxyRequestRunnable(final Socket clientSocket) {
				this.clientSocket = clientSocket;
			}

			@Override
			public void run() {
				try {
					logger.trace("start");

					final InputStream clientInputStream = clientSocket.getInputStream();
					final String line = readLine(clientInputStream);
					logger.info("proxy-request: " + line);
					final String header = readHeader(clientInputStream);
					logger.trace("header parsed");

					final Socket remoteSocket = new Socket(proxyUtil.parseHostname(line), proxyUtil.parsePort(line));
					final OutputStream remoteOutputStream = remoteSocket.getOutputStream();
					remoteOutputStream.write(line.getBytes());
					remoteOutputStream.write(NEWLINE.getBytes());
					remoteOutputStream.write(header.getBytes());
					remoteOutputStream.flush();
					logger.trace("send header to remote");

					final InputStream remoteInputStream = remoteSocket.getInputStream();
					final OutputStream clientOutputStream = clientSocket.getOutputStream();
					streamUtil.copy(remoteInputStream, clientOutputStream);

					logger.trace("done");
				} catch (IOException | ParseException e) {
					logger.trace(e.getClass().getName(), e);
				} finally {
					try {
						clientSocket.close();
					} catch (IOException e) {
						// nop
					}
				}
			}
		}
	}
}
