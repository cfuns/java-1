package de.benjaminborbe.monitoring.check;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.slf4j.Logger;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

public class TcpCheck implements MonitoringCheck {

	private static final int TIMEOUT = 5 * 1000;

	private final int port;

	private final String hostname;

	private final Logger logger;

	private final String name;

	public TcpCheck(final Logger logger, final String name, final String hostname, final int port) {
		this.logger = logger;
		this.name = name;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public MonitoringCheckResult check() {
		Socket socket = null;
		try {
			socket = new Socket();
			final SocketAddress endpoint = new InetSocketAddress(hostname, port);
			socket.connect(endpoint, TIMEOUT);
			if (socket.isConnected()) {
				final String msg = "connected successful to " + hostname + ":" + port;
				logger.trace(msg);
				return new CheckResultImpl(this, true, msg, null);
			}
			else {
				final String msg = "connecting failed to " + hostname + ":" + port;
				logger.warn(msg);
				return new CheckResultImpl(this, false, msg, null);
			}
		}
		catch (final SocketTimeoutException e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because SocketTimeoutException";
			logger.trace(msg);
			return new CheckResultImpl(this, false, msg, null);
		}
		catch (final UnknownHostException e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because UnknownHostException";
			logger.trace(msg);
			return new CheckResultImpl(this, false, msg, null);
		}
		catch (final Exception e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because " + e.getClass().getSimpleName();
			logger.trace(msg, e);
			return new CheckResultImpl(this, false, msg, null);
		}
		finally {
			try {
				if (socket != null)
					socket.close();
			}
			catch (final IOException e) {
				logger.trace("IOException while close socket", e);
			}
		}
	}

	@Override
	public String getDescription() {
		return "TCP-Check host: " + hostname + ":" + port;
	}

	@Override
	public String getName() {
		return name;
	}

}
