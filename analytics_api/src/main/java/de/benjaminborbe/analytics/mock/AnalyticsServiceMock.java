package de.benjaminborbe.analytics.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsService;

@Singleton
public class AnalyticsServiceMock implements AnalyticsService {

	@Inject
	public AnalyticsServiceMock() {
	}

	@Override
	public void execute() {
	}
}
