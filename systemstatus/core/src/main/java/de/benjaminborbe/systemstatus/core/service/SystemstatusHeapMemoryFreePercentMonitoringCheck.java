package de.benjaminborbe.systemstatus.core.service;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintLongGE;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintLongLE;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import de.benjaminborbe.systemstatus.core.util.SystemstatusMemoryUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SystemstatusHeapMemoryFreePercentMonitoringCheck implements MonitoringCheck {

	private static final String MEMORY_FREE_PERCENT = "memory_free_percent";

	public static final String ID = "261a1009-f632-4a90-b2c4-a1bab46424c5";

	private final SystemstatusMemoryUtil systemstatusMemoryUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final ParseUtil parseUtil;

	private final Logger logger;

	@Inject
	public SystemstatusHeapMemoryFreePercentMonitoringCheck(
		final Logger logger,
		final SystemstatusMemoryUtil systemstatusMemoryUtil,
		final ValidationConstraintValidator validationConstraintValidator,
		final ParseUtil parseUtil
	) {
		this.logger = logger;
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
		return "HeapMemoryFreePercent";
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(MEMORY_FREE_PERCENT);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		try {
			final SystemstatusMemoryUsage systemstatusMemoryUsage = systemstatusMemoryUtil.getMemoryUsage();
			final DecimalFormat df = new DecimalFormat("#####0.0");
			final double freePercent = (1d * systemstatusMemoryUsage.getHeapMax() - systemstatusMemoryUsage.getHeapUsed()) / systemstatusMemoryUsage.getHeapMax() * 100;
			logger.debug("(" + systemstatusMemoryUsage.getHeapMax() + " - " + systemstatusMemoryUsage.getHeapUsed() + ") / " + systemstatusMemoryUsage.getHeapMax() + " * 100 = "
				+ freePercent);

			final long expectedFreePercent = getFreePercent(parameter);
			if (freePercent >= expectedFreePercent) {
				return new MonitoringCheckResultDto(this, true);
			} else {
				return new MonitoringCheckResultDto(this, false, "free memory (" + df.format(freePercent) + "%) less expected free memory (" + expectedFreePercent + "%)");
			}
		} catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, e);
		}
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return getTitle() + "-Check on " + parameter.get(MEMORY_FREE_PERCENT) + "%";
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<ValidationError>();

		// memory_free_mb
		{
			try {
				final long memoryFreePercent = getFreePercent(parameter);
				final List<ValidationConstraint<Long>> constraints = new ArrayList<ValidationConstraint<Long>>();
				constraints.add(new ValidationConstraintNotNull<Long>());
				constraints.add(new ValidationConstraintLongGE(0));
				constraints.add(new ValidationConstraintLongLE(100));
				result.addAll(validationConstraintValidator.validate(MEMORY_FREE_PERCENT, memoryFreePercent, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple(MEMORY_FREE_PERCENT + " invalid"));
			}
		}

		return result;
	}

	public long getFreePercent(final Map<String, String> parameter) throws ParseException {
		return parseUtil.parseLong(parameter.get(MEMORY_FREE_PERCENT));
	}
}
