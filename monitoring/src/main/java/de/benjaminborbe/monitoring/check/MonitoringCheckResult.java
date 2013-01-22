package de.benjaminborbe.monitoring.check;

public interface MonitoringCheckResult {

	Boolean getSuccessful();

	String getMessage();

	Exception getException();

}
