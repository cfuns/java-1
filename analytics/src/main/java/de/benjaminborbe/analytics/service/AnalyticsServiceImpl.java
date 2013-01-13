package de.benjaminborbe.analytics.service;

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
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.dao.AnalyticsReportBean;
import de.benjaminborbe.analytics.dao.AnalyticsReportDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportLogDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportValueDao;
import de.benjaminborbe.analytics.util.AnalyticsAggregator;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.queue.Queue;
import de.benjaminborbe.tools.queue.QueueBuilder;
import de.benjaminborbe.tools.queue.QueueConsumer;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class AnalyticsServiceImpl implements AnalyticsService {

	private final class AddMessage {

		private final AnalyticsReportIdentifier reportIdentifier;

		private final AnalyticsReportValue reportValue;

		public AddMessage(final AnalyticsReportIdentifier reportIdentifier, final AnalyticsReportValue reportValue) {
			this.reportIdentifier = reportIdentifier;
			this.reportValue = reportValue;
		}

		public AnalyticsReportValue getReportValue() {
			return reportValue;
		}

		public AnalyticsReportIdentifier getReportIdentifier() {
			return reportIdentifier;
		}
	}

	private final class AddMessageConsumer implements QueueConsumer<AddMessage> {

		@Override
		public void consume(final AddMessage message) {
			try {
				analyticsReportLogDao.addReportValue(message.getReportIdentifier(), message.getReportValue());
			}
			catch (final StorageException e) {
				logger.warn(e.getClass().getName());
			}
		}
	}

	private static final int DURATION_WARN = 1;

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final AnalyticsReportDao analyticsReportDao;

	private final ValidationExecutor validationExecutor;

	private final AnalyticsReportValueDao analyticsReportValueDao;

	private final CalendarUtil calendarUtil;

	private final AnalyticsAggregator analyticsAggregator;

	private final AnalyticsReportLogDao analyticsReportLogDao;

	private final Queue<AddMessage> queue;

	private final DurationUtil durationUtil;

	@Inject
	public AnalyticsServiceImpl(
			final Logger logger,
			final DurationUtil durationUtil,
			final QueueBuilder queueBuilder,
			final AnalyticsAggregator analyticsAggregator,
			final CalendarUtil calendarUtil,
			final AuthorizationService authorizationService,
			final ValidationExecutor validationExecutor,
			final AnalyticsReportDao analyticsReportDao,
			final AnalyticsReportLogDao analyticsReportLogDao,
			final AnalyticsReportValueDao analyticsReportValueDao) {
		this.logger = logger;
		this.durationUtil = durationUtil;
		this.analyticsAggregator = analyticsAggregator;
		this.calendarUtil = calendarUtil;
		this.authorizationService = authorizationService;
		this.analyticsReportDao = analyticsReportDao;
		this.validationExecutor = validationExecutor;
		this.analyticsReportLogDao = analyticsReportLogDao;
		this.analyticsReportValueDao = analyticsReportValueDao;

		queue = queueBuilder.buildQueue(new AddMessageConsumer());
	}

	@Override
	public AnalyticsReportValueIterator getReportIterator(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier,
			final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("getReport");

			return analyticsReportValueDao.valueIterator(analyticsReportIdentifier, analyticsReportInterval);
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
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
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			addReportValue(analyticsReportIdentifier, 1d);
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
			addReportValue(analyticsReportIdentifier, new AnalyticsReportValueDto(calendarUtil.now(), value));
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
			addReportValue(analyticsReportIdentifier, new AnalyticsReportValueDto(calendarUtil.now(), new Long(value).doubleValue()));
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
			logger.debug("addReportValue");

			queue.put(new AddMessage(analyticsReportIdentifier, reportValue));
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
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("getReports");

			final List<AnalyticsReport> result = new ArrayList<AnalyticsReport>();
			final EntityIterator<AnalyticsReportBean> i = analyticsReportDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
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
	public void createReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportDto report) throws AnalyticsServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("createReport");

			final AnalyticsReportBean bean = analyticsReportDao.create();
			bean.setId(new AnalyticsReportIdentifier(report.getName()));
			bean.setName(report.getName());
			bean.setAggregation(report.getAggregation());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("Bookmark " + errors.toString());
				throw new ValidationException(errors);
			}
			analyticsReportDao.save(bean);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
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
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("deleteReport");
			analyticsReportValueDao.delete(analyticsIdentifier);
			analyticsReportLogDao.delete(analyticsIdentifier);
			analyticsReportDao.delete(analyticsIdentifier);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
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
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("aggreate");
			analyticsAggregator.aggregate();
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

}
