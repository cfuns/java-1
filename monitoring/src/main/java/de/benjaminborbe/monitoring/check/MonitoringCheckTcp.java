package de.benjaminborbe.monitoring.check;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class MonitoringCheckTcp implements MonitoringCheck {

	private static final String PORT = "port";

	private static final String HOSTNAME = "hostname";

	private static final String TIMEOUT = "timeout";

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public MonitoringCheckTcp(final Logger logger, final ParseUtil parseUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	public MonitoringCheckType getType() {
		return MonitoringCheckType.TCP;
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(HOSTNAME, PORT);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		final String hostname = parameter.get(HOSTNAME);
		final int port;
		try {
			port = parseUtil.parseInt(parameter.get(PORT));
		}
		catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, false, "illegal paremter " + PORT);
		}
		final int timeout;
		try {
			timeout = parseUtil.parseInt(parameter.get(TIMEOUT));
		}
		catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, false, "illegal paremter " + TIMEOUT);
		}
		return check(hostname, port, timeout);
	}

	private MonitoringCheckResult check(final String hostname, final int port, final int timeout) {
		Socket socket = null;
		try {
			socket = new Socket();
			final SocketAddress endpoint = new InetSocketAddress(hostname, port);
			socket.connect(endpoint, timeout);
			if (socket.isConnected()) {
				final String msg = "connected successful to " + hostname + ":" + port;
				logger.trace(msg);
				return new MonitoringCheckResultDto(this, true, msg);
			}
			else {
				final String msg = "connecting failed to " + hostname + ":" + port;
				logger.warn(msg);
				return new MonitoringCheckResultDto(this, false, msg);
			}
		}
		catch (final SocketTimeoutException e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because SocketTimeoutException";
			logger.trace(msg);
			return new MonitoringCheckResultDto(this, false, msg);
		}
		catch (final UnknownHostException e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because UnknownHostException";
			logger.trace(msg);
			return new MonitoringCheckResultDto(this, false, msg);
		}
		catch (final Exception e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because " + e.getClass().getSimpleName();
			logger.trace(msg, e);
			return new MonitoringCheckResultDto(this, false, msg);
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

}
