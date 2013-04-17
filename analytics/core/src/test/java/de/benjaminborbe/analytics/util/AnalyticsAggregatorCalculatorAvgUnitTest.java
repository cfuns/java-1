package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AnalyticsAggregatorCalculatorAvgUnitTest {

	@Test
	public void testAggreate() throws Exception {
		final AnalyticsAggregatorCalculatorAvg calc = new AnalyticsAggregatorCalculatorAvg();
		final Calendar calendar = Calendar.getInstance();
		final List<AnalyticsReportValue> values = new ArrayList<>();

		{
			final AnalyticsReportValue result = calc.aggregate(calendar, values);
			assertEquals(new Long(0), result.getCounter());
			assertEquals(new Double(0), result.getValue());
			assertEquals(calendar, result.getDate());
		}

		values.add(buildValue(calendar, 2d, 1l));

		{
			final AnalyticsReportValue result = calc.aggregate(calendar, values);
			assertEquals(new Double(2), result.getValue());
			assertEquals(new Long(1), result.getCounter());
			assertEquals(calendar, result.getDate());
		}

		values.add(buildValue(calendar, 2d, 2l));

		{
			final AnalyticsReportValue result = calc.aggregate(calendar, values);
			assertEquals(new Double(2), result.getValue());
			assertEquals(new Long(3), result.getCounter());
			assertEquals(calendar, result.getDate());
		}

		values.add(buildValue(calendar, 4d, 3l));

		{
			final AnalyticsReportValue result = calc.aggregate(calendar, values);
			assertEquals(new Double(3), result.getValue());
			assertEquals(new Long(6), result.getCounter());
			assertEquals(calendar, result.getDate());
		}

	}

	private AnalyticsReportValue buildValue(final Calendar calendar, final Double value, final Long counter) {
		return new AnalyticsReportValueDto(calendar, value, counter);
	}
}
