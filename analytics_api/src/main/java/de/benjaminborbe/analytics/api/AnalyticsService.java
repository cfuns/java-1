package de.benjaminborbe.analytics.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface AnalyticsService {

	void addReportData(SessionIdentifier sessionIdentifier, AnalyticsReportIdentifier analyticsReportIdentifier, ReportValue reportValue) throws AnalyticsServiceException,
			PermissionDeniedException, LoginRequiredException;

	ReportValueIterator getReportIterator(SessionIdentifier sessionIdentifier, AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException,
			PermissionDeniedException, LoginRequiredException;

	Collection<AnalyticsReport> getReports(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	void createReport(SessionIdentifier sessionIdentifier, AnalyticsReportDto report) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException,
			ValidationException;

}
