package de.benjaminborbe.analytics.mock;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportDto;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsReportValueListIterator;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

@Singleton
public class AnalyticsServiceMock implements AnalyticsService {

	@Inject
	public AnalyticsServiceMock() {
	}

	@Override
	public Collection<AnalyticsReport> getReports(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public void createReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportDto report) throws AnalyticsServiceException, PermissionDeniedException,
			LoginRequiredException {
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportValue reportValue) throws AnalyticsServiceException {
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException {
	}

	@Override
	public void deleteReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsIdentifier) throws AnalyticsServiceException,
			PermissionDeniedException, LoginRequiredException {
	}

	@Override
	public void aggreate(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
	}

	@Override
	public AnalyticsReportValueIterator getReportIterator(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier,
			final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final long time) throws AnalyticsServiceException {
	}

	@Override
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final double value) throws AnalyticsServiceException {
	}

	@Override
	public AnalyticsReportValueIterator getReportIteratorFillMissing(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier,
			final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public void expectAnalyticsViewPermission(final SessionIdentifier sessionIdentifier) {
	}

	@Override
	public void expectAnalyticsAdminPermission(final SessionIdentifier sessionIdentifier) {
	}

	@Override
	public boolean hasAnalyticsViewPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		return false;
	}

	@Override
	public boolean hasAnalyticsAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		return false;
	}

	@Override
	public boolean hasAnalyticsViewOrAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException {
		return false;
	}

	@Override
	public void expectAnalyticsViewOrAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, AnalyticsServiceException {
	}

	@Override
	public Collection<String> getLogWithoutReport(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public AnalyticsReportValueListIterator getReportListIterator(final SessionIdentifier sessionIdentifier, final List<AnalyticsReportIdentifier> analyticsReportIdentifiers,
			final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public AnalyticsReportValueListIterator getReportListIteratorFillMissing(final SessionIdentifier sessionIdentifier,
			final List<AnalyticsReportIdentifier> analyticsReportIdentifiers, final AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException,
			PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public AnalyticsReportIdentifier createAnalyticsReportIdentifier(final String id) throws AnalyticsServiceException {
		return null;
	}

	@Override
	public void rebuildReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException,
			PermissionDeniedException, LoginRequiredException {
	}

	@Override
	public void rebuildReports(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
	}

}
