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
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.dao.AnalyticsReportBean;
import de.benjaminborbe.analytics.dao.AnalyticsReportDao;
import de.benjaminborbe.analytics.dao.AnalyticsReportValueDao;
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
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class AnalyticsServiceImpl implements AnalyticsService {

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final AnalyticsReportDao analyticsReportDao;

	private final ValidationExecutor validationExecutor;

	private final AnalyticsReportValueDao analyticsReportValueDao;

	private final CalendarUtil calendarUtil;

	@Inject
	public AnalyticsServiceImpl(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final AuthorizationService authorizationService,
			final AnalyticsReportDao analyticsReportDao,
			final ValidationExecutor validationExecutor,
			final AnalyticsReportValueDao analyticsReportValueDao) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.authorizationService = authorizationService;
		this.analyticsReportDao = analyticsReportDao;
		this.validationExecutor = validationExecutor;
		this.analyticsReportValueDao = analyticsReportValueDao;
	}

	@Override
	public AnalyticsReportValueIterator getReportIterator(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier)
			throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("getReport");

			return analyticsReportValueDao.valueIterator(analyticsReportIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
		}
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException {
		addReportValue(analyticsReportIdentifier, 1d);
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final Double value) throws AnalyticsServiceException {
		addReportValue(analyticsReportIdentifier, new AnalyticsReportValueDto(calendarUtil.now(), value));
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws AnalyticsServiceException {
		try {
			logger.debug("addReportValue");

			analyticsReportValueDao.addReportValue(analyticsReportIdentifier, reportValue);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final ParseException e) {
			throw new AnalyticsServiceException(e);
		}
		finally {
		}
	}

	@Override
	public Collection<AnalyticsReport> getReports(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
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
		}
	}

	@Override
	public void createReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportDto report) throws AnalyticsServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException {
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
	}

	@Override
	public void deleteReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsIdentifier) throws AnalyticsServiceException,
			PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("deleteReport");
			analyticsReportDao.delete(analyticsIdentifier);
		}
		catch (final StorageException e) {
			throw new AnalyticsServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new AnalyticsServiceException(e);
		}
	}

}
