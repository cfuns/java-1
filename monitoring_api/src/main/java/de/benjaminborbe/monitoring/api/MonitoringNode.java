package de.benjaminborbe.monitoring.api;

import java.util.Calendar;
import java.util.Map;

public interface MonitoringNode extends MonitoringHasParentId {

	MonitoringNodeIdentifier getId();

	String getName();

	MonitoringCheckType getCheckType();

	Map<String, String> getParameter();

	Boolean getSilent();

	Boolean getActive();

	Boolean getResult();

	String getMessage();

	String getDescription();

	Integer getFailureCounter();

	Calendar getLastCheck();

}
