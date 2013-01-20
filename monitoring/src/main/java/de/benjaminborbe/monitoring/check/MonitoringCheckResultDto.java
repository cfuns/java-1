package de.benjaminborbe.monitoring.check;

public class MonitoringCheckResultDto implements MonitoringCheckResult {

	private boolean successful;

	private String message;

	private MonitoringCheck check;

	public MonitoringCheckResultDto() {
	}

	public MonitoringCheckResultDto(final MonitoringCheck check, final boolean successful, final String message) {
		this.check = check;
		this.successful = successful;
		this.message = message;
	}

	@Override
	public boolean isSuccessful() {
		return successful;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setSuccessful(final boolean successful) {
		this.successful = successful;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public MonitoringCheck getCheck() {
		return check;
	}

	public void setCheck(final MonitoringCheck check) {
		this.check = check;
	}

}
