package de.benjaminborbe.monitoring.check;

import java.net.URL;

public class MonitoringCheckResultDto implements MonitoringCheckResult {

	private boolean successful;

	private String message;

	private MonitoringCheck check;

	private URL url;

	private Exception exception;

	public MonitoringCheckResultDto() {
	}

	public MonitoringCheckResultDto(final MonitoringCheck check, final boolean successful, final String message) {
		this.check = check;
		this.successful = successful;
		this.message = message;
	}

	public MonitoringCheckResultDto(final MonitoringCheck check, final boolean successful, final String message, final URL url) {
		this.check = check;
		this.successful = successful;
		this.message = message;
		this.url = url;
	}

	public MonitoringCheckResultDto(final MonitoringCheckHttp check, final Exception exception, final URL url) {
		this.check = check;
		this.successful = false;
		this.setException(exception);
		this.url = url;
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

	@Override
	public MonitoringCheck getCheck() {
		return check;
	}

	public void setCheck(final MonitoringCheck check) {
		this.check = check;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	@Override
	public Exception getException() {
		return exception;
	}

	public void setException(final Exception exception) {
		this.exception = exception;
	}

}
