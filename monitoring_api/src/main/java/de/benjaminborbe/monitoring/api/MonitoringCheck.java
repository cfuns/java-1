package de.benjaminborbe.monitoring.api;

public interface MonitoringCheck {

	String getName();

	String getDescription();

	MonitoringCheckResult check();

}
