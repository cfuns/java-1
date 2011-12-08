package de.benjaminborbe.monitoring.check;

public class RetryCheck implements Check {

	private final Check check;

	private final int retryLimit;

	public RetryCheck(final Check check, final int retryLimit) {
		this.check = check;
		this.retryLimit = retryLimit;

	}

	@Override
	public boolean check() {
		for (int i = 0; i < retryLimit; ++i) {
			if (check.check()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getMessage() {
		return check.getMessage();
	}

}
