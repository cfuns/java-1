package de.benjaminborbe.analytics.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.api.ReportValue;
import de.benjaminborbe.analytics.util.ReportValueImpl;
import de.benjaminborbe.authentication.api.SessionIdentifier;

@Singleton
public class AnalyticsServiceImpl implements AnalyticsService {

	private final Logger logger;

	@Inject
	public AnalyticsServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public List<ReportValue> getReport(final SessionIdentifier sessionIdentifier) throws AnalyticsServiceException {
		logger.trace("execute");

		final List<ReportValue> result = new ArrayList<ReportValue>();
		result.add(new ReportValueImpl("2012-01", 12d));
		result.add(new ReportValueImpl("2012-02", 1d));
		result.add(new ReportValueImpl("2012-03", 10d));
		result.add(new ReportValueImpl("2012-04", 11d));
		result.add(new ReportValueImpl("2012-05", 13d));
		result.add(new ReportValueImpl("2012-06", 14d));
		result.add(new ReportValueImpl("2012-07", 5d));
		result.add(new ReportValueImpl("2012-08", 6d));
		result.add(new ReportValueImpl("2012-09", 3d));
		result.add(new ReportValueImpl("2012-10", 20d));
		result.add(new ReportValueImpl("2012-11", 22d));
		result.add(new ReportValueImpl("2012-12", 11d));
		return result;
	}

}
