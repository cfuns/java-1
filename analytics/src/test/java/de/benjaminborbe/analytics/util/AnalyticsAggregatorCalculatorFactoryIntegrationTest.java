package de.benjaminborbe.analytics.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.inject.Injector;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.guice.AnalyticsModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

@RunWith(Parameterized.class)
public class AnalyticsAggregatorCalculatorFactoryIntegrationTest {

	private final AnalyticsReportAggregation analyticsReportAggregation;

	@Parameters(name = "{index} - {0}")
	public static Collection<Object[]> generateData() {
		final List<Object[]> result = new ArrayList<Object[]>();
		for (final AnalyticsReportAggregation analyticsReportAggregation : AnalyticsReportAggregation.values()) {
			result.add(new Object[] { analyticsReportAggregation });
		}
		return result;
	}

	public AnalyticsAggregatorCalculatorFactoryIntegrationTest(final AnalyticsReportAggregation analyticsReportAggregation) {
		this.analyticsReportAggregation = analyticsReportAggregation;
	}

	@Test
	public void testGet() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AnalyticsModulesMock());
		final AnalyticsAggregatorCalculatorFactory analyticsAggregatorCalculatorFactory = injector.getInstance(AnalyticsAggregatorCalculatorFactory.class);
		assertNotNull(analyticsAggregatorCalculatorFactory.get(analyticsReportAggregation));
	}
}
