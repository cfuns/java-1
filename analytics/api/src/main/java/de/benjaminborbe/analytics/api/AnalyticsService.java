package de.benjaminborbe.analytics.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.util.Collection;
import java.util.List;

public interface AnalyticsService {

	String PERMISSION_ADMIN = "analyticsAdmin";

	String PERMISSION_VIEW = "analyticsView";

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier) throws AnalyticsServiceException;

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, AnalyticsReportValue reportValue) throws AnalyticsServiceException;

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, double value) throws AnalyticsServiceException;

	void addReportValue(AnalyticsReportIdentifier analyticsReportIdentifier, long value) throws AnalyticsServiceException;

	void aggreate(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	AnalyticsReportIdentifier createAnalyticsReportIdentifier(String id) throws AnalyticsServiceException;

	void createReport(
		SessionIdentifier sessionIdentifier,
		AnalyticsReportDto report
	) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException,
		ValidationException;

	void deleteReport(
		SessionIdentifier sessionIdentifier,
		AnalyticsReportIdentifier analyticsIdentifier
	) throws AnalyticsServiceException, PermissionDeniedException,
		LoginRequiredException;

	void expectAnalyticsAdminPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, AnalyticsServiceException;

	void expectAnalyticsViewOrAdminPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, AnalyticsServiceException;

	void expectAnalyticsViewPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, AnalyticsServiceException;

	Collection<String> getLogWithoutReport(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	AnalyticsReportValueIterator getReportIterator(
		SessionIdentifier sessionIdentifier, AnalyticsReportIdentifier analyticsReportIdentifier,
		AnalyticsReportInterval analyticsReportInterval
	) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	AnalyticsReportValueIterator getReportIteratorFillMissing(
		SessionIdentifier sessionIdentifier, AnalyticsReportIdentifier analyticsReportIdentifier,
		AnalyticsReportInterval analyticsReportInterval
	) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	AnalyticsReportValueListIterator getReportListIterator(
		SessionIdentifier sessionIdentifier, List<AnalyticsReportIdentifier> analyticsReportIdentifiers,
		AnalyticsReportInterval analyticsReportInterval
	) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	AnalyticsReportValueListIterator getReportListIteratorFillMissing(
		SessionIdentifier sessionIdentifier, List<AnalyticsReportIdentifier> analyticsReportIdentifiers,
		AnalyticsReportInterval analyticsReportInterval
	) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	Collection<AnalyticsReport> getReports(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;

	boolean hasAnalyticsAdminPermission(SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException;

	boolean hasAnalyticsViewOrAdminPermission(SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException;

	boolean hasAnalyticsViewPermission(SessionIdentifier sessionIdentifier) throws LoginRequiredException, AnalyticsServiceException;

	void rebuildReport(
		SessionIdentifier sessionIdentifier,
		AnalyticsReportIdentifier analyticsReportIdentifier
	) throws AnalyticsServiceException, PermissionDeniedException,
		LoginRequiredException;

	void rebuildReports(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException;
}
