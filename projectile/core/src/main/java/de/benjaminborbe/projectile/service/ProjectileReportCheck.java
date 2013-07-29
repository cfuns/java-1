package de.benjaminborbe.projectile.service;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.projectile.dao.ProjectileReportBean;
import de.benjaminborbe.projectile.dao.ProjectileReportDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CurrentTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProjectileReportCheck implements MonitoringCheck {

	public static final String ID = "7eecb245-cc68-4385-9a19-73e9a5f9ba62";

	private final ProjectileReportDao projectileReportDao;

	private final CurrentTime currentTime;

	private final static int WEEK_LIMIT = 36;

	private final static int MONTH_LIMIT = 36;

	private final static int YEAR_LIMIT = 36;

	private final static String USERNAME = "bborbe";

	private final Logger logger;

	@Inject
	public ProjectileReportCheck(final Logger logger, final CurrentTime currentTime, final ProjectileReportDao projectileReportDao) {
		this.logger = logger;
		this.currentTime = currentTime;
		this.projectileReportDao = projectileReportDao;
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}

	@Override
	public String getTitle() {
		return "ProjectileReportCheck";
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList();
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		try {
			final ProjectileReportBean report = projectileReportDao.getReportForUser(new UserIdentifier(USERNAME));
			final long weekAgeInHours = getAgeInHours(report.getWeekUpdateDate());
			if (weekAgeInHours > WEEK_LIMIT) {
				final String msg = "week report for " + USERNAME + " is expired! " + weekAgeInHours + " > " + WEEK_LIMIT + " hours";
				logger.debug(msg);
				return new MonitoringCheckResultDto(this, false, msg);
			}
			final long monthAgeInHours = getAgeInHours(report.getMonthUpdateDate());
			if (monthAgeInHours > MONTH_LIMIT) {
				final String msg = "week report for " + USERNAME + " is expired! " + monthAgeInHours + " > " + MONTH_LIMIT + " hours";
				logger.debug(msg);
				return new MonitoringCheckResultDto(this, false, msg);
			}
			final long yearAgeInHours = getAgeInHours(report.getYearUpdateDate());
			if (yearAgeInHours > YEAR_LIMIT) {
				final String msg = "week report for " + USERNAME + " is expired! " + yearAgeInHours + " > " + YEAR_LIMIT + " hours";
				logger.debug(msg);
				return new MonitoringCheckResultDto(this, false, msg);
			}
			return new MonitoringCheckResultDto(this, true);
		} catch (final StorageException e) {
			return new MonitoringCheckResultDto(this, e);
		}
	}

	protected long getAgeInHours(final Calendar calendar) {
		return (currentTime.currentTimeMillis() - calendar.getTimeInMillis()) / 1000 / 60 / 60;
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return getTitle();
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<ValidationError>();
		return result;
	}

}
