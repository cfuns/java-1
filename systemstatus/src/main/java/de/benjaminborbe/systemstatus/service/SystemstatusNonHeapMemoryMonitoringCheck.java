package de.benjaminborbe.systemstatus.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

public class SystemstatusNonHeapMemoryMonitoringCheck implements MonitoringCheck {

	public static final String ID = "c50f8ab4-47e8-4929-897e-e6edc897ff4a";

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}

	@Override
	public String getTitle() {
		return "NonHeapMemory";
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
