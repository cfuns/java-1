package de.benjaminborbe.systemstatus.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

public class SystemStatusMonitoringCheck implements MonitoringCheck {

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(getClass().getName());
	}

	@Override
	public String getTitle() {
		return "DiskSpace";
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList();
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		return new MonitoringCheckResult() {

			@Override
			public Boolean getSuccessful() {
				return true;
			}

			@Override
			public String getMessage() {
				return null;
			}

			@Override
			public Exception getException() {
				return null;
			}
		};
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return "-";
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		return Arrays.asList();
	}

}
