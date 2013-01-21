package de.benjaminborbe.monitoring.api;

import java.net.URL;

public interface MonitoringNodeResult {

	public boolean isSuccessful();

	public String getMessage();

	public URL getUrl();

	public Exception getException();

	public String getName();

	public MonitoringNodeIdentifier getId();

}
