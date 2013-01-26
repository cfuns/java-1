package de.benjaminborbe.monitoring.api;

public interface MonitoringCheckResult {

	Boolean getSuccessful();

	String getMessage();

	Exception getException();

}
