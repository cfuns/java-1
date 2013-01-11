package de.benjaminborbe.analytics.api;

import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface AnalyticsService {

	List<ReportValue> getReport(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException;
}
