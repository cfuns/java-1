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
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.ReportValue;
import de.benjaminborbe.analytics.api.ReportValueIterator;
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
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class AnalyticsServiceImpl implements AnalyticsService {

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final AnalyticsReportDao analyticsReportDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	private final AnalyticsReportValueDao analyticsReportValueDao;

	@Inject
	public AnalyticsServiceImpl(
			final Logger logger,
			final AuthorizationService authorizationService,
			final AnalyticsReportDao analyticsReportDao,
			final IdGeneratorUUID idGeneratorUUID,
			final ValidationExecutor validationExecutor,
			final AnalyticsReportValueDao analyticsReportValueDao) {
		this.logger = logger;
		this.authorizationService = authorizationService;
		this.analyticsReportDao = analyticsReportDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.validationExecutor = validationExecutor;
		this.analyticsReportValueDao = analyticsReportValueDao;
	}

	@Override
	public ReportValueIterator getReportIterator(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier)
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
	public void addReportData(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier, final ReportValue reportValue)
			throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("addData");

			analyticsReportValueDao.addData(analyticsReportIdentifier, reportValue);
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
			bean.setId(new AnalyticsReportIdentifier(idGeneratorUUID.nextId()));
			bean.setName(report.getName());

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

}
