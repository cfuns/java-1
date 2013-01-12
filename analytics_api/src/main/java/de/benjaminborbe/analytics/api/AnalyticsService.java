package de.benjaminborbe.analytics.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface AnalyticsService {

	AnalyticsReportValueIterator getReportIterator(SessionIdentifier sessionIdentifier, AnalyticsReportIdentifier analyticsReportIdentifier,
			AnalyticsReportInterval analyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	Collection<AnalyticsReport> getReports(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	void createReport(SessionIdentifier sessionIdentifier, AnalyticsReportDto report) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException,
			ValidationException;

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, AnalyticsReportValue reportValue) throws AnalyticsServiceException;

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException;

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, long time) throws AnalyticsServiceException;

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, double value) throws AnalyticsServiceException;

	void deleteReport(SessionIdentifier sessionIdentifier, AnalyticsReportIdentifier analyticsIdentifier) throws AnalyticsServiceException, PermissionDeniedException,
			LoginRequiredException;

	void aggreate(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

}
