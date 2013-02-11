package de.benjaminborbe.analytics.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportDto;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.dao.AnalyticsReportBean;
import de.benjaminborbe.analytics.dao.AnalyticsReportDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportLogDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportValueDao;
import de.benjaminborbe.analytics.util.AddValueAction;
import de.benjaminborbe.analytics.util.AnalyticsAggregator;
import de.benjaminborbe.analytics.util.AnalyticsIntervalUtil;
import de.benjaminborbe.analytics.util.AnalyticsReportValueIteratorFillMissingValues;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class AnalyticsServiceImpl implements AnalyticsService {

	private static final int DURATION_WARN = 300;

	private final AddValueAction addValueAction;

	private final AnalyticsAggregator analyticsAggregator;

	private final AnalyticsIntervalUtil analyticsIntervalUtil;

	private final AnalyticsReportDao analyticsReportDao;

	private final AnalyticsReportLogDao analyticsReportLogDao;

	private final AnalyticsReportValueDao analyticsReportValueDao;

	private final AuthorizationService authorizationService;

	private final DurationUtil durationUtil;

	private final Logger logger;

	private final ValidationExecutor validationExecutor;

	@Inject
	public AnalyticsServiceImpl(
			final Logger logger,
			final AnalyticsIntervalUtil analyticsIntervalUtil,
			final AddValueAction addValueAction,
			final DurationUtil durationUtil,
			final AnalyticsAggregator analyticsAggregator,
			final CalendarUtil calendarUtil,
			final AuthorizationService authorizationService,
			final ValidationExecutor validationExecutor,
			final AnalyticsReportDao analyticsReportDao,
			final AnalyticsReportLogDao analyticsReportLogDao,
			final AnalyticsReportValueDao analyticsReportValueDao) {
		this.logger = logger;
		this.analyticsIntervalUtil = analyticsIntervalUtil;
		this.addValueAction = addValueAction;
		this.durationUtil = durationUtil;
		this.analyticsAggregator = analyticsAggregator;
		this.authorizationService = authorizationService;
		this.analyticsReportDao = analyticsReportDao;
		this.validationExecutor = validationExecutor;
		this.analyticsReportLogDao = analyticsReportLogDao;
		this.analyticsReportValueDao = analyticsReportValueDao;
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.trace("addReportValue");
			addValueAction.addReportValue(analyticsReportIdentifier);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws AnalyticsServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.trace("addReportValue");
			addValueAction.addReportValue(analyticsReportIdentifier, reportValue);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final double value) throws AnalyticsServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.trace("addReportValue");
			addValueAction.addReportValue(analyticsReportIdentifier, value);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final long value) throws AnalyticsServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.trace("addReportValue");
			addValueAction.addReportValue(analyticsReportIdentifier, value);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void aggreate(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminRole(sessionIdentifier);
			logger.debug("aggreate");
			analyticsAggregator.aggregate();
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void createReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportDto report) throws AnalyticsServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminRole(sessionIdentifier);
			logger.debug("createReport");

			final AnalyticsReportBean bean = analyticsReportDao.create();
			bean.setId(new AnalyticsReportIdentifier(report.getName() + AnalyticsReportDao.SEPERATOR + report.getAggregation()));
			bean.setName(report.getName());
			bean.setAggregation(report.getAggregation());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("createReport " + errors.toString());
				throw new ValidationException(errors);
			}
			analyticsReportDao.save(bean);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsIdentifier) throws AnalyticsServiceException,
			PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminRole(sessionIdentifier);
			logger.debug("deleteReport");
			analyticsReportValueDao.delete(analyticsIdentifier);
			analyticsReportLogDao.delete(analyticsIdentifier);
			analyticsReportDao.delete(analyticsIdentifier);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectAnalyticsAdminRole(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, AnalyticsServiceException {
		try {
			authorizationService.expectRole(sessionIdentifier, new RoleIdentifier(ANALYTICS_ROLE_ADMIN));
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public void expectAnalyticsViewOrAdminRole(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, AnalyticsServiceException {
		try {
			authorizationService.expectOneOfRoles(sessionIdentifier, new RoleIdentifier(ANALYTICS_ROLE_ADMIN), new RoleIdentifier(ANALYTICS_ROLE_VIEW));
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public void expectAnalyticsViewRole(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectRole(sessionIdentifier, new RoleIdentifier(ANALYTICS_ROLE_VIEW));
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public AnalyticsReportValueIterator getReportIterator(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier,
			final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsViewRole(sessionIdentifier);
			logger.debug("getReportIterator");

			return analyticsReportValueDao.valueIterator(analyticsReportIdentifier, analyticsReportInterval);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public AnalyticsReportValueIterator getReportIteratorFillMissing(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier,
			final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsViewRole(sessionIdentifier);
			logger.debug("getReportIteratorFillMissing");

			final AnalyticsReportBean report = analyticsReportDao.load(analyticsReportIdentifier);
			return new AnalyticsReportValueIteratorFillMissingValues(analyticsIntervalUtil, analyticsReportValueDao.valueIterator(analyticsReportIdentifier, analyticsReportInterval),
					report.getAggregation(), analyticsReportInterval);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<AnalyticsReport> getReports(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsViewRole(sessionIdentifier);
			logger.debug("getReports");

			final List<AnalyticsReport> result = new ArrayList<AnalyticsReport>();
			final EntityIterator<AnalyticsReportBean> i = analyticsReportDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean hasAnalyticsAdminRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		try {
			return authorizationService.hasRole(sessionIdentifier, new RoleIdentifier(ANALYTICS_ROLE_ADMIN));
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public boolean hasAnalyticsViewOrAdminRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		try {
			return authorizationService.hasOneOfRoles(sessionIdentifier, new RoleIdentifier(ANALYTICS_ROLE_ADMIN), new RoleIdentifier(ANALYTICS_ROLE_VIEW));
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public boolean hasAnalyticsViewRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		try {
			return authorizationService.hasRole(sessionIdentifier, new RoleIdentifier(ANALYTICS_ROLE_VIEW));
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public Collection<String> getLogWithoutReport(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminRole(sessionIdentifier);
			logger.debug("getLogWithoutReport");

			final List<String> result = new ArrayList<String>();
			final StorageIterator i = analyticsReportLogDao.reportNameIterator();
			while (i.hasNext()) {
				final String name = i.next().getString();
				if (!analyticsReportDao.existsReportWithName(name)) {
					result.add(name);
				}
			}
			return result;
		}
		catch (final UnsupportedEncodingException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final IdentifierIteratorException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
