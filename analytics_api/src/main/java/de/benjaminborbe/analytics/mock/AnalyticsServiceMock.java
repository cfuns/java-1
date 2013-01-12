package de.benjaminborbe.analytics.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportDto;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
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
	public void addReportValue(final AnalyticsReportIdentifier analyticsReportIdentifier, final Double value) throws AnalyticsServiceException {
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

}
