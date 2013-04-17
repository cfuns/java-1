package de.benjaminborbe.analytics.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportDto;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsReportValueListIterator;
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
import de.benjaminborbe.analytics.util.AnalyticsReportValueListIteratorImpl;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		} finally {
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
		} finally {
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
		} finally {
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
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void aggreate(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminPermission(sessionIdentifier);
			logger.debug("aggreate");
			analyticsAggregator.aggregate();
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void createReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportDto report) throws AnalyticsServiceException, PermissionDeniedException,
		LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminPermission(sessionIdentifier);
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
		} catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsIdentifier) throws AnalyticsServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminPermission(sessionIdentifier);
			logger.debug("deleteReport");
			analyticsReportValueDao.delete(analyticsIdentifier);
			analyticsReportLogDao.delete(analyticsIdentifier);
			analyticsReportDao.delete(analyticsIdentifier);
		} catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectAnalyticsAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, AnalyticsServiceException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_ADMIN));
		} catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public void expectAnalyticsViewOrAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, AnalyticsServiceException {
		try {
			authorizationService.expectOneOfPermissions(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_ADMIN),
				authorizationService.createPermissionIdentifier(PERMISSION_VIEW));
		} catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public void expectAnalyticsViewPermission(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_VIEW));
		} catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public AnalyticsReportValueIterator getReportIterator(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier,
																												final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsViewPermission(sessionIdentifier);
			logger.debug("getReportIterator");

			return analyticsReportValueDao.valueIterator(analyticsReportIdentifier, analyticsReportInterval);
		} catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public AnalyticsReportValueIterator getReportIteratorFillMissing(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier,
																																	 final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsViewPermission(sessionIdentifier);
			logger.debug("getReportIteratorFillMissing - report: " + analyticsReportIdentifier + " interval: " + analyticsReportInterval);

			final AnalyticsReportBean report = analyticsReportDao.load(analyticsReportIdentifier);
			return new AnalyticsReportValueIteratorFillMissingValues(analyticsIntervalUtil, analyticsReportValueDao.valueIterator(analyticsReportIdentifier, analyticsReportInterval),
				report.getAggregation(), analyticsReportInterval);
		} catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<AnalyticsReport> getReports(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsViewPermission(sessionIdentifier);
			logger.debug("getReports");

			final List<AnalyticsReport> result = new ArrayList<>();
			final EntityIterator<AnalyticsReportBean> i = analyticsReportDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final StorageException | EntityIteratorException e) {
			throw new AnalyticsServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean hasAnalyticsAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_ADMIN));
		} catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public boolean hasAnalyticsViewOrAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		try {
			return authorizationService.hasOneOfPermissions(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_ADMIN),
				authorizationService.createPermissionIdentifier(PERMISSION_VIEW));
		} catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public boolean hasAnalyticsViewPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_VIEW));
		} catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

	@Override
	public Collection<String> getLogWithoutReport(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminPermission(sessionIdentifier);
			logger.debug("getLogWithoutReport");

			final List<String> result = new ArrayList<>();
			final StorageIterator i = analyticsReportLogDao.reportNameIterator();
			while (i.hasNext()) {
				final String name = i.next().getString();
				if (!analyticsReportDao.existsReportWithName(name)) {
					result.add(name);
				}
			}
			return result;
		} catch (final UnsupportedEncodingException | IdentifierIteratorException | StorageException e) {
			throw new AnalyticsServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public AnalyticsReportValueListIterator getReportListIterator(final SessionIdentifier sessionIdentifier, final List<AnalyticsReportIdentifier> analyticsReportIdentifiers,
																																final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsViewPermission(sessionIdentifier);
			logger.debug("getReportListIterator - interval: " + analyticsReportInterval);

			final List<AnalyticsReportValueIterator> analyticsReportValueIterators = new ArrayList<>();
			for (final AnalyticsReportIdentifier analyticsReportIdentifier : analyticsReportIdentifiers) {
				analyticsReportValueIterators.add(getReportIterator(sessionIdentifier, analyticsReportIdentifier, analyticsReportInterval));
			}
			return new AnalyticsReportValueListIteratorImpl(analyticsReportValueIterators);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public AnalyticsReportValueListIterator getReportListIteratorFillMissing(final SessionIdentifier sessionIdentifier,
																																					 final List<AnalyticsReportIdentifier> analyticsReportIdentifiers, final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsViewPermission(sessionIdentifier);
			logger.debug("getReportListIteratorFillMissing - interval: " + analyticsReportInterval);

			final List<AnalyticsReportValueIterator> analyticsReportValueIterators = new ArrayList<>();
			for (final AnalyticsReportIdentifier analyticsReportIdentifier : analyticsReportIdentifiers) {
				analyticsReportValueIterators.add(getReportIteratorFillMissing(sessionIdentifier, analyticsReportIdentifier, analyticsReportInterval));
			}
			return new AnalyticsReportValueListIteratorImpl(analyticsReportValueIterators);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public AnalyticsReportIdentifier createAnalyticsReportIdentifier(final String id) throws AnalyticsServiceException {
		return id != null ? new AnalyticsReportIdentifier(id) : null;
	}

	@Override
	public void rebuildReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminPermission(sessionIdentifier);
			logger.debug("rebuildReport - id: " + analyticsReportIdentifier);

			final AnalyticsReportValueIterator i = getReportIterator(sessionIdentifier, analyticsReportIdentifier, AnalyticsReportInterval.MINUTE);
			final List<AnalyticsReportValue> values = new ArrayList<>();
			while (i.hasNext()) {
				final AnalyticsReportValue value = i.next();
				values.add(value);
			}
			logger.debug("found " + values.size() + " values");

			final AnalyticsReport analyticsReport = analyticsReportDao.load(analyticsReportIdentifier);
			analyticsAggregator.rebuildReport(analyticsReport, values);
		} catch (final StorageException | ParseException | UnsupportedEncodingException e) {
			throw new AnalyticsServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void rebuildReports(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAnalyticsAdminPermission(sessionIdentifier);
			logger.debug("rebuildRepors");

			final IdentifierIterator<AnalyticsReportIdentifier> i = analyticsReportDao.getIdentifierIterator();
			while (i.hasNext()) {
				rebuildReport(sessionIdentifier, i.next());
			}
		} catch (final StorageException | IdentifierIteratorException e) {
			throw new AnalyticsServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
