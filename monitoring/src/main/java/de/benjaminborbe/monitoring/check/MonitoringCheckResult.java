package de.benjaminborbe.monitoring.check;

import java.net.URL;

public interface MonitoringCheckResult {

	public boolean isSuccessful();

	public String getMessage();

	public MonitoringCheck getCheck();

	public URL getUrl();

	public Exception getException();

}
