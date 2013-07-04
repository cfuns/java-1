package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class MonitoringCheckNop implements MonitoringCheck {

	public static final String ID = "NOP";

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList();
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		return new MonitoringCheckResultDto(this, true, "nop");
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return "-";
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		return Arrays.asList();
	}

	@Override
	public String getTitle() {
		return "Nop";
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}
}
