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
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SystemstatusDiskspaceFreeMbMonitoringCheck implements MonitoringCheck {

	public static final String ID = "1bf40451-e72a-47ed-9f2c-51fe1234f4b9";

	private static final String DISKSPACE_FREE_MB = "diskspace_free_mb";

	private final ParseUtil parseUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final SystemstatusPartitionUtil systemstatusPartitionUtil;

	private final Logger logger;

	@Inject
	public SystemstatusDiskspaceFreeMbMonitoringCheck(
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
		return "DiskSpaceFreeMb";
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(DISKSPACE_FREE_MB);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		try {
			for (final SystemstatusPartition partition : systemstatusPartitionUtil.getPartitions()) {
				logger.debug(partition.getAbsolutePath());

				final long expectedFreeSpace = getFreeMb(parameter);
				final long freeSpace = partition.getFreeSpace() / 1024 / 1024;

				if (freeSpace >= expectedFreeSpace) {
					return new MonitoringCheckResultDto(this, true);
				} else {
					return new MonitoringCheckResultDto(this, false, "free diskspacek (" + freeSpace + "mb) less expected free diskspace (" + expectedFreeSpace + "mb)");
				}
			}
			return new MonitoringCheckResultDto(this, false, "no filesystem found");
		} catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, e);
		}
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return getTitle() + "-Check on " + parameter.get(DISKSPACE_FREE_MB) + "mb";
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<>();

		// memory_free_mb
		{
			try {
				final long memoryFreeMb = getFreeMb(parameter);
				final List<ValidationConstraint<Long>> constraints = new ArrayList<>();
				constraints.add(new ValidationConstraintNotNull<Long>());
				constraints.add(new ValidationConstraintLongGE(0));
				result.addAll(validationConstraintValidator.validate(DISKSPACE_FREE_MB, memoryFreeMb, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple(DISKSPACE_FREE_MB + " invalid"));
			}
		}

		return result;
	}

	public long getFreeMb(final Map<String, String> parameter) throws ParseException {
		return parseUtil.parseLong(parameter.get(DISKSPACE_FREE_MB));
	}
}
