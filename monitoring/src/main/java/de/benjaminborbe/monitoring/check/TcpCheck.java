package de.benjaminborbe.monitoring.check;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;

public class TcpCheck implements Check {

	private static final int TIMEOUT = 5 * 1000;

	private final int port;

	private final String hostname;

	private final Logger logger;

	public TcpCheck(final Logger logger, final String hostname, final int port) {
		this.logger = logger;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public CheckResult check() {
		Socket socket = null;
		try {
			socket = new Socket();
			final SocketAddress endpoint = new InetSocketAddress(hostname, port);
			socket.connect(endpoint, TIMEOUT);
			if (socket.isConnected()) {
				final String msg = "connected successful to " + hostname + ":" + port;
				logger.debug(msg);
				return new CheckResultImpl(this, true, msg);
			}
			else {
				final String msg = "connecting failed to " + hostname + ":" + port;
				logger.warn(msg);
				return new CheckResultImpl(this, false, msg);
			}
		}
		catch (final Exception e) {
			logger.warn("check tcp-connect to " + hostname + ":" + port + " failed");
			return new CheckResultImpl(this, false, "connecting failed to " + hostname + ":" + port);
		}
		finally {
			try {
				if (socket != null)
					socket.close();
			}
			catch (final IOException e) {
				logger.debug("IOException while close socket", e);
			}
		}
	}

	@Override
	public String getDescription() {
		return "TCP-Check host: " + hostname + ":" + port;
	}

}
