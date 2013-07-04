package de.benjaminborbe.analytics.gui.chart;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AnalyticsReportChartColorGeneratorUnitTest {

	@Test
	public void testAmount0() throws Exception {
		final AnalyticsReportChartColorGenerator analyticsReportChartColorGenerator = new AnalyticsReportChartColorGenerator();
		final int amount = 0;
		final List<String> colors = analyticsReportChartColorGenerator.getColors(amount);
		assertNotNull(colors);
		assertEquals(amount, colors.size());
	}

	@Test
	public void testAmount3() throws Exception {
		final AnalyticsReportChartColorGenerator analyticsReportChartColorGenerator = new AnalyticsReportChartColorGenerator();
		final int amount = 3;
		final List<String> colors = analyticsReportChartColorGenerator.getColors(amount);
		assertNotNull(colors);
		assertEquals(amount, colors.size());
	}

	@Test
	public void testAmountMax() throws Exception {
		final AnalyticsReportChartColorGenerator analyticsReportChartColorGenerator = new AnalyticsReportChartColorGenerator();
		final int amount = Integer.MAX_VALUE;
		final List<String> colors = analyticsReportChartColorGenerator.getColors(amount);
		assertNotNull(colors);
		assertEquals(255 * 6, colors.size());
	}

	@Test
	public void testColors3() throws Exception {
		final AnalyticsReportChartColorGenerator analyticsReportChartColorGenerator = new AnalyticsReportChartColorGenerator();
		final int amount = 3;
		final List<String> colors = analyticsReportChartColorGenerator.getColors(amount);
		assertNotNull(colors);
		assertEquals(amount, colors.size());
		assertEquals("#FF0000", colors.get(0));
		assertEquals("#0000FF", colors.get(1));
		assertEquals("#00FF00", colors.get(2));
	}

}
