package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.api.CheckResult;

public class RetryCheck implements Check {

	private final Check check;

	private final int retryLimit;

	public RetryCheck(final Check check, final int retryLimit) {
		this.check = check;
		this.retryLimit = retryLimit;

	}

	@Override
	public CheckResult check() {
		CheckResult result = null;
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
