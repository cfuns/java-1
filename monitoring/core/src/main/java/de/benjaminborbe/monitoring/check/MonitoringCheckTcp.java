package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerGE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerLE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MonitoringCheckTcp implements MonitoringCheck {

	public static final String ID = "TCP";

	private static final String PORT = "port";

	private static final String HOSTNAME = "hostname";

	private static final String TIMEOUT = "timeout";

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public MonitoringCheckTcp(final Logger logger, final ParseUtil parseUtil, final ValidationConstraintValidator validationConstraintValidator) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(HOSTNAME, PORT, TIMEOUT);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		final String hostname = parameter.get(HOSTNAME);
		final int port;
		try {
			port = parseUtil.parseInt(parameter.get(PORT));
		} catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, false, "illegal paremter " + PORT);
		}
		final int timeout;
		try {
			timeout = parseUtil.parseInt(parameter.get(TIMEOUT));
		} catch (final ParseException e) {
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
			} else {
				final String msg = "connecting failed to " + hostname + ":" + port;
				logger.warn(msg);
				return new MonitoringCheckResultDto(this, false, msg);
			}
		} catch (final SocketTimeoutException e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because SocketTimeoutException";
			logger.trace(msg);
			return new MonitoringCheckResultDto(this, false, msg);
		} catch (final UnknownHostException e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because UnknownHostException";
			logger.trace(msg);
			return new MonitoringCheckResultDto(this, false, msg);
		} catch (final Exception e) {
			final String msg = "connecting failed to " + hostname + ":" + port + " because " + e.getClass().getSimpleName();
			logger.trace(msg, e);
			return new MonitoringCheckResultDto(this, false, msg);
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (final IOException e) {
				logger.trace("IOException while close socket", e);
			}
		}
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		final String hostname = parameter.get(HOSTNAME);
		final String port = parameter.get(PORT);
		return "TCP-Check on " + hostname + ":" + port;
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<>();

		// hostname
		{
			final String hostname = parameter.get(HOSTNAME);
			final List<ValidationConstraint<String>> constraints = new ArrayList<>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("hostname", hostname, constraints));
		}

		// port
		{
			try {
				final int port = parseUtil.parseInt(parameter.get(PORT));
				final List<ValidationConstraint<Integer>> constraints = new ArrayList<>();
				constraints.add(new ValidationConstraintNotNull<Integer>());
				constraints.add(new ValidationConstraintIntegerGE(0x1));
				constraints.add(new ValidationConstraintIntegerLE(0xFFFF));
				result.addAll(validationConstraintValidator.validate("port", port, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple("port invalid"));
			}
		}

		// timeout
		{
			try {
				final int timeout = parseUtil.parseInt(parameter.get(TIMEOUT));
				final List<ValidationConstraint<Integer>> constraints = new ArrayList<>();
				constraints.add(new ValidationConstraintNotNull<Integer>());
				constraints.add(new ValidationConstraintIntegerGE(0));
				constraints.add(new ValidationConstraintIntegerLE(60000));
				result.addAll(validationConstraintValidator.validate("timeout", timeout, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple("timeout invalid"));
			}
		}

		return result;
	}

	@Override
	public String getTitle() {
		return "Tcp";
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}
}
