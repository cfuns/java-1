package de.benjaminborbe.analytics.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportDto;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.ReportValue;
import de.benjaminborbe.analytics.api.ReportValueIterator;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

@Singleton
public class AnalyticsServiceMock implements AnalyticsService {

	@Inject
	public AnalyticsServiceMock() {
	}

	@Override
	public void addReportData(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier, final ReportValue reportValue)
			throws AnalyticsServiceException {
	}

	@Override
	public ReportValueIterator getReportIterator(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier)
			throws AnalyticsServiceException {
		return null;
	}

	@Override
	public Collection<AnalyticsReport> getReports(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public void createReport(final SessionIdentifier sessionIdentifier, final AnalyticsReportDto report) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
	}

}
