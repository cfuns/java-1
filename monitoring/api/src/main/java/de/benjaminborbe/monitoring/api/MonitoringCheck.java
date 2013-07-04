package de.benjaminborbe.monitoring.api;

import de.benjaminborbe.api.ValidationError;

import java.util.Collection;
import java.util.Map;

public interface MonitoringCheck {

	MonitoringCheckIdentifier getId();

	String getTitle();

	Collection<String> getRequireParameters();

	MonitoringCheckResult check(Map<String, String> parameter);

	String getDescription(Map<String, String> parameter);

	Collection<ValidationError> validate(Map<String, String> parameter);
}
