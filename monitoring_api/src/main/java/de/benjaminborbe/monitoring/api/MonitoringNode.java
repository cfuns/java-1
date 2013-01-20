package de.benjaminborbe.monitoring.api;

public interface MonitoringNode {

	MonitoringNodeIdentifier getId();

	String getName();

	MonitoringCheckType getCheckType();
}
