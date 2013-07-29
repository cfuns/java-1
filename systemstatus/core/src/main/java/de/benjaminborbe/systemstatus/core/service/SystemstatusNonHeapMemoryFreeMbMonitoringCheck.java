package de.benjaminborbe.systemstatus.core.service;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintLongGE;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import de.benjaminborbe.systemstatus.core.util.SystemstatusMemoryUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SystemstatusNonHeapMemoryFreeMbMonitoringCheck implements MonitoringCheck {

	private static final String MEMORY_FREE_MB = "memory_free_mb";

	public static final String ID = "73890240-379c-4e45-8151-8d92e097ea9b";

	private final SystemstatusMemoryUtil systemstatusMemoryUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final ParseUtil parseUtil;

	@Inject
	public SystemstatusNonHeapMemoryFreeMbMonitoringCheck(
		final SystemstatusMemoryUtil systemstatusMemoryUtil,
		final ValidationConstraintValidator validationConstraintValidator,
		final ParseUtil parseUtil
	) {
		this.systemstatusMemoryUtil = systemstatusMemoryUtil;
		this.validationConstraintValidator = validationConstraintValidator;
		this.parseUtil = parseUtil;
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}

	@Override
	public String getTitle() {
		return "NonHeapMemoryFreeMb";
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(MEMORY_FREE_MB);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		try {
			final SystemstatusMemoryUsage systemstatusMemoryUsage = systemstatusMemoryUtil.getMemoryUsage();
			final long free = (systemstatusMemoryUsage.getNonHeapMax() - systemstatusMemoryUsage.getNonHeapUsed()) / 1024 / 1024;
			final long expectedFree = getFreeMb(parameter);
			if (free >= expectedFree) {
				return new MonitoringCheckResultDto(this, true);
			} else {
				return new MonitoringCheckResultDto(this, false, "free memory (" + free + "mb) less expected free memory (" + expectedFree + "mb)");
			}
		} catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, e);
		}
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return getTitle() + "-Check on " + parameter.get(MEMORY_FREE_MB) + "mb";
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<ValidationError>();

		// memory_free_mb
		{
			try {
				final long memoryFreeMb = getFreeMb(parameter);
				final List<ValidationConstraint<Long>> constraints = new ArrayList<ValidationConstraint<Long>>();
				constraints.add(new ValidationConstraintNotNull<Long>());
				constraints.add(new ValidationConstraintLongGE(0));
				result.addAll(validationConstraintValidator.validate(MEMORY_FREE_MB, memoryFreeMb, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple(MEMORY_FREE_MB + " invalid"));
			}
		}

		return result;
	}

	public long getFreeMb(final Map<String, String> parameter) throws ParseException {
		return parseUtil.parseLong(parameter.get(MEMORY_FREE_MB));
	}
}
