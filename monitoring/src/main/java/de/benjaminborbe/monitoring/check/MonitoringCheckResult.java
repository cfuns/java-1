package de.benjaminborbe.monitoring.check;

public interface MonitoringCheckResult {

	boolean isSuccessful();

	String getMessage();
}
