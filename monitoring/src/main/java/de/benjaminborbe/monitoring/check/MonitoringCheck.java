package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.Map;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.monitoring.api.MonitoringCheckType;

public interface MonitoringCheck {

	MonitoringCheckType getType();

	Collection<String> getRequireParameters();

	MonitoringCheckResult check(Map<String, String> parameter);

	String getDescription(Map<String, String> parameter);

	Collection<ValidationError> validate(Map<String, String> parameter);
}
