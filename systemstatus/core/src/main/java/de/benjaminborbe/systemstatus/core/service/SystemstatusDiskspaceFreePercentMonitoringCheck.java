package de.benjaminborbe.systemstatus.core.service;

import com.google.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.systemstatus.api.SystemstatusPartition;
import de.benjaminborbe.systemstatus.core.util.SystemstatusPartitionUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintLongGE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintLongLE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import org.slf4j.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SystemstatusDiskspaceFreePercentMonitoringCheck implements MonitoringCheck {

	public static final String ID = "4c44d6b8-d2b6-4ef1-9b41-151520e09596";

	private static final String DISKSPACE_FREE_PERCENT = "diskspace_free_percent";

	private final ParseUtil parseUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final SystemstatusPartitionUtil systemstatusPartitionUtil;

	private final Logger logger;

	@Inject
	public SystemstatusDiskspaceFreePercentMonitoringCheck(
		final Logger logger,
		final ParseUtil parseUtil,
		final ValidationConstraintValidator validationConstraintValidator,
		final SystemstatusPartitionUtil systemstatusPartitionUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.validationConstraintValidator = validationConstraintValidator;
		this.systemstatusPartitionUtil = systemstatusPartitionUtil;
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}

	@Override
	public String getTitle() {
		return "DiskSpaceFreePercent";
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(DISKSPACE_FREE_PERCENT);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		try {
			for (final SystemstatusPartition partition : systemstatusPartitionUtil.getPartitions()) {
				logger.debug(partition.getAbsolutePath());
				final DecimalFormat df = new DecimalFormat("#####0.0");

				final double freePercent = (1d * partition.getFreeSpace() / partition.getTotalSpace() * 100);
				logger.debug(partition.getFreeSpace() + " / " + partition.getTotalSpace() + " * 100 = " + +freePercent);

				final long expectedFreePercent = getFreePercent(parameter);
				if (freePercent >= expectedFreePercent) {
					return new MonitoringCheckResultDto(this, true);
				} else {
					return new MonitoringCheckResultDto(this, false, "free space (" + df.format(freePercent) + "%) less expected free space (" + expectedFreePercent + "%)");
				}
			}
			return new MonitoringCheckResultDto(this, false, "no filesystem found");
		} catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, e);
		}
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return getTitle() + "-Check on " + parameter.get(DISKSPACE_FREE_PERCENT) + "%";
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
				result.addAll(validationConstraintValidator.validate(DISKSPACE_FREE_PERCENT, memoryFreePercent, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple(DISKSPACE_FREE_PERCENT + " invalid"));
			}
		}

		return result;
	}

	public long getFreePercent(final Map<String, String> parameter) throws ParseException {
		return parseUtil.parseLong(parameter.get(DISKSPACE_FREE_PERCENT));
	}

}
