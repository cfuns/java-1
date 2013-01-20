package de.benjaminborbe.monitoring.check;

import java.net.URL;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

public class CheckResultImpl implements MonitoringCheckResult {

	private static final long serialVersionUID = 6741576691093237313L;

	private final boolean success;

	private final String message;

	private final MonitoringCheck check;

	private final URL url;

	public CheckResultImpl(final MonitoringCheck check, final boolean success, final String message, final URL url) {
		this.check = check;
		this.success = success;
		this.message = message;
		this.url = url;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getDescription() {
		return check.getDescription();
	}

	@Override
	public MonitoringCheck getCheck() {
		return check;
	}

	@Override
	public String toString() {
		final StringBuffer content = new StringBuffer();
		if (isSuccess()) {
			content.append("[OK] ");
		}
		else {
			content.append("[FAIL] ");
		}
		content.append(getName());
		content.append(" - ");
		content.append(getMessage());
		return content.toString();
	}

	@Override
	public String getName() {
		return check.getName();
	}

	@Override
	public URL getUrl() {
		return url;
	}
}
