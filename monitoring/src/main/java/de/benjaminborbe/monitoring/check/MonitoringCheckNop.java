package de.benjaminborbe.monitoring.check;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;

public class MonitoringCheckNop implements MonitoringCheck {

	@Override
	public MonitoringCheckType getType() {
		return MonitoringCheckType.NOP;
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList();
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		return null;
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return "-";
	}
}
