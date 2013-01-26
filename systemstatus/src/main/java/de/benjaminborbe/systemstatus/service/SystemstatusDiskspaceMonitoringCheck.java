package de.benjaminborbe.systemstatus.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

public class SystemstatusDiskspaceMonitoringCheck implements MonitoringCheck {

	public static final String ID = "b4e45cd4-a3d5-4971-b281-3106602c8c8d";

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
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
