package de.benjaminborbe.monitoring.api;

import java.util.Map;

public interface MonitoringNode {

	MonitoringNodeIdentifier getId();

	String getName();

	MonitoringCheckType getCheckType();

	Map<String, String> getParameter();

	Boolean getSilent();

	Boolean getActive();

	Boolean getResult();

	String getMessage();

	String getDescription();

	MonitoringNodeIdentifier getParentId();
}
