package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

public class RetryCheck implements MonitoringCheck {

	private final MonitoringCheck check;

	private final int retryLimit;

	public RetryCheck(final MonitoringCheck check, final int retryLimit) {
		this.check = check;
		this.retryLimit = retryLimit;

	}

	@Override
	public MonitoringCheckResult check() {
		MonitoringCheckResult result = null;
		for (int i = 0; i < retryLimit; ++i) {
			result = check.check();
			if (result.isSuccess()) {
				return result;
			}
		}
		return result;
	}

	@Override
	public String getDescription() {
		return check.getDescription();
	}

	@Override
	public String getName() {
		return check.getName();
	}

}
