package de.benjaminborbe.analytics.api;

import java.util.Calendar;
import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface AnalyticsService {

	List<ReportValue> getReport(SessionIdentifier sessionIdentifier) throws AnalyticsServiceException;

	void addData(SessionIdentifier sessionIdentifier, Calendar calendar, double value) throws AnalyticsServiceException;
}
