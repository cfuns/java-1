package de.benjaminborbe.analytics.mock;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.ReportValue;
import de.benjaminborbe.authentication.api.SessionIdentifier;

@Singleton
public class AnalyticsServiceMock implements AnalyticsService {

	@Inject
	public AnalyticsServiceMock() {
	}

	@Override
	public List<ReportValue> getReport(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException {
		return null;
	}

}
