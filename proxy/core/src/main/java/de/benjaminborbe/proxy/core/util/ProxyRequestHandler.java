package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.proxy.api.ProxyConversationIdentifier;
import de.benjaminborbe.proxy.core.dao.ProxyContentImpl;
import de.benjaminborbe.proxy.core.dao.ProxyConversationImpl;
import de.benjaminborbe.tools.stream.InputStreamCopy;
import de.benjaminborbe.tools.stream.OutputStreamCopy;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ThreadRunner;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;

public class ProxyRequestHandler {

	private static final String NEWLINE = "\r\n";

	private final Logger logger;

	private final DurationUtil durationUtil;

	private final ProxyLineReader proxyLineReader;

	private final ProxyLineParser proxyLineParser;

	private final ProxyConversationNotifier proxyConversationNotifier;

	private final StreamUtil streamUtil;

	private final ParseUtil parseUtil;

	private final IdGeneratorUUID idGenerator;

	private final ThreadRunner threadRunner;

	@Inject
	public ProxyRequestHandler(
		final Logger logger,
		final DurationUtil durationUtil,
		final ProxyLineReader proxyLineReader,
		final ProxyLineParser proxyLineParser,
		final ProxyConversationNotifier proxyConversationNotifier,
		final StreamUtil streamUtil,
		final ParseUtil parseUtil,
		final IdGeneratorUUID idGenerator,
		final ThreadRunner threadRunner
	) {
		this.logger = logger;
		this.durationUtil = durationUtil;
		this.proxyLineReader = proxyLineReader;
		this.proxyLineParser = proxyLineParser;
		this.proxyConversationNotifier = proxyConversationNotifier;
		this.streamUtil = streamUtil;
		this.parseUtil = parseUtil;
		this.idGenerator = idGenerator;
		this.threadRunner = threadRunner;
	}

	public void handleRequest(final Socket clientSocket) {
		Socket remoteSocket = null;
		try {
			logger.trace("handle proxy request started");
			final Duration duration = durationUtil.getDuration();
			final ProxyContentImpl requestContent = new ProxyContentImpl();
			final ProxyContentImpl responseContent = new ProxyContentImpl();

			final InputStream clientInputStream = clientSocket.getInputStream();
			final String line = proxyLineReader.readLine(clientInputStream);
			logger.debug("proxy-request: " + line);

			final String hostname = proxyLineParser.parseHostname(line);
			final int port = proxyLineParser.parsePort(line);
			logger.debug("connected to " + hostname + ":" + port);
			remoteSocket = new Socket(hostname, port);

			final OutputStream remoteOutputStream = new OutputStreamCopy(remoteSocket.getOutputStream(), requestContent.getOutputStream());
			remoteOutputStream.write(line.getBytes());
			remoteOutputStream.write(NEWLINE.getBytes());

			final String header = readHeader(clientInputStream);
			logger.trace("header parsed");

			remoteOutputStream.write(header.getBytes());
			remoteOutputStream.flush();
			logger.trace("send header to remote");

			final InputStream remoteInputStream = new InputStreamCopy(remoteSocket.getInputStream(), responseContent.getOutputStream());
			final OutputStream clientOutputStream = clientSocket.getOutputStream();
			streamUtil.copy(remoteInputStream, clientOutputStream);
			clientOutputStream.flush();

			threadRunner.run("proxyConversationNotifier", new ProxyConversationNotifierRunnable(requestContent, responseContent, line, duration));

			logger.debug("handle proxy request finished");
		} catch (IOException e) {
			logger.debug(e.getClass().getName(), e);
		} catch (ParseException e) {
			logger.debug(e.getClass().getName(), e);
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				// nop
			}
			try {
				remoteSocket.close();
			} catch (IOException e) {
				// nop
			}
		}
	}

	private ProxyConversationIdentifier createNewId() {
		return new ProxyConversationIdentifier(idGenerator.nextId());
	}

	private String readHeader(final InputStream is) throws IOException {
		final StringWriter stringWriter = new StringWriter();
		String line;
		do {
			line = proxyLineReader.readLine(is);
			stringWriter.append(line);
			stringWriter.append(NEWLINE);
		} while (line != null && line.length() > 1);
		if (line != null) {
			stringWriter.append(line);
			stringWriter.append(NEWLINE);
		}
		return stringWriter.toString();
	}

	private class ProxyConversationNotifierRunnable implements Runnable {

		private final ProxyContentImpl requestContent;

		private final ProxyContentImpl responseContent;

		private final String line;

		private final Duration duration;

		public ProxyConversationNotifierRunnable(
			final ProxyContentImpl requestContent,
			final ProxyContentImpl responseContent,
			final String line,
			final Duration duration
		) {
			this.requestContent = requestContent;
			this.responseContent = responseContent;
			this.line = line;
			this.duration = duration;
		}

		@Override
		public void run() {
			try {
				final ProxyConversationImpl proxyConversation = new ProxyConversationImpl(createNewId(), requestContent, responseContent);
				proxyConversation.setUrl(parseUtil.parseURL(proxyLineParser.parseUrl(line)));
				proxyConversation.setDuration(duration.getTime());
				proxyConversationNotifier.onProxyConversationCompleted(proxyConversation);
			} catch (ParseException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
	}
}
