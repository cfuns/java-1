package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.tools.iterator.IteratorByList;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnalyticsReportValueIteratorFillMissingValuesUnitTest {

	private final class I extends IteratorByList<AnalyticsReportValue, AnalyticsServiceException> implements AnalyticsReportValueIterator {

		public I(final List<AnalyticsReportValue> values) {
			super(values);
		}

		public I(final AnalyticsReportValue[] values) {
			super(values);
		}
	}

	@Test
	public void testFillIterator() throws Exception {
		final Double v = new Double(1);
		final AnalyticsReportValue[] values = {buildAnalyticsReportValue(2012, 0, v), buildAnalyticsReportValue(2009, 0, v)};
		final AnalyticsReportValueIterator analyticsReportValueIterator = new I(values);
		assertTrue(analyticsReportValueIterator.hasNext());
		assertEquals(2012, analyticsReportValueIterator.next().getDate().get(Calendar.YEAR));
		assertTrue(analyticsReportValueIterator.hasNext());
		assertEquals(2009, analyticsReportValueIterator.next().getDate().get(Calendar.YEAR));
		assertFalse(analyticsReportValueIterator.hasNext());
	}

	@Test
	public void testFillWithMissingValues() throws Exception {
		final Double v = new Double(1);
		final AnalyticsReportInterval analyticsReportInterval = AnalyticsReportInterval.YEAR;
		final AnalyticsIntervalUtil analyticsIntervalUtil = new AnalyticsIntervalUtil();
		final AnalyticsReportValue[] values = {buildAnalyticsReportValue(2012, 0, v), buildAnalyticsReportValue(2009, 0, v)};
		final AnalyticsReportValueIterator analyticsReportValueIterator = new I(values);
		final AnalyticsReportAggregation analyticsReportAggregation = AnalyticsReportAggregation.SUM;
		final AnalyticsReportValueIteratorFillMissingValues i = new AnalyticsReportValueIteratorFillMissingValues(analyticsIntervalUtil, analyticsReportValueIterator,
			analyticsReportAggregation, analyticsReportInterval);

		{
			assertTrue(i.hasNext());
		}
		{
			final AnalyticsReportValue value = i.next();
			assertEquals(2012, value.getDate().get(Calendar.YEAR));
			assertEquals(v, value.getValue());
		}
		{
			assertTrue(i.hasNext());
		}
		{
			final AnalyticsReportValue value = i.next();
			assertEquals(2011, value.getDate().get(Calendar.YEAR));
			assertEquals(new Double(0), value.getValue());
		}
		{
			assertTrue(i.hasNext());
		}
		{
			final AnalyticsReportValue value = i.next();
			assertEquals(2010, value.getDate().get(Calendar.YEAR));
			assertEquals(new Double(0), value.getValue());
		}
		{
			assertTrue(i.hasNext());
		}
		{
			final AnalyticsReportValue value = i.next();
			assertEquals(2009, value.getDate().get(Calendar.YEAR));
			assertEquals(v, value.getValue());
		}
		{
			assertFalse(i.hasNext());
		}
	}

	@Test
	public void testFillWithUnnecessaryValues() throws Exception {
		final AnalyticsReportInterval analyticsReportInterval = AnalyticsReportInterval.YEAR;
		final AnalyticsIntervalUtil analyticsIntervalUtil = new AnalyticsIntervalUtil();
		final List<AnalyticsReportValue> values = new ArrayList<AnalyticsReportValue>();
		values.add(buildAnalyticsReportValue(2012, 1, new Double(1)));
		values.add(buildAnalyticsReportValue(2012, 0, new Double(2)));
		values.add(buildAnalyticsReportValue(2009, 4, new Double(3)));
		values.add(buildAnalyticsReportValue(2009, 3, new Double(4)));
		values.add(buildAnalyticsReportValue(2009, 2, new Double(5)));
		values.add(buildAnalyticsReportValue(2009, 1, new Double(6)));
		values.add(buildAnalyticsReportValue(2009, 0, new Double(7)));

		final AnalyticsReportValueIterator analyticsReportValueIterator = new I(values);
		final AnalyticsReportAggregation analyticsReportAggregation = AnalyticsReportAggregation.SUM;
		final AnalyticsReportValueIteratorFillMissingValues i = new AnalyticsReportValueIteratorFillMissingValues(analyticsIntervalUtil, analyticsReportValueIterator,
			analyticsReportAggregation, analyticsReportInterval);

		{
			assertTrue(i.hasNext());
		}
		{
			final AnalyticsReportValue value = i.next();
			assertEquals(2012, value.getDate().get(Calendar.YEAR));
			assertEquals(new Double(2), value.getValue());
		}
		{
			assertTrue(i.hasNext());
		}
		{
			final AnalyticsReportValue value = i.next();
			assertEquals(2011, value.getDate().get(Calendar.YEAR));
			assertEquals(new Double(0), value.getValue());
		}
		{
			assertTrue(i.hasNext());
		}
		{
			final AnalyticsReportValue value = i.next();
			assertEquals(2010, value.getDate().get(Calendar.YEAR));
			assertEquals(new Double(0), value.getValue());
		}
		{
			assertTrue(i.hasNext());
		}
		{
			final AnalyticsReportValue value = i.next();
			assertEquals(2009, value.getDate().get(Calendar.YEAR));
			assertEquals(new Double(7), value.getValue());
		}
		{
			assertFalse(i.hasNext());
		}
	}

	private AnalyticsReportValue buildAnalyticsReportValue(final int year, final int month, final Double value) {
		final Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);

		final AnalyticsReportValue analyticsReportValue = EasyMock.createMock(AnalyticsReportValue.class);
		EasyMock.expect(analyticsReportValue.getValue()).andReturn(value).anyTimes();
		EasyMock.expect(analyticsReportValue.getDate()).andReturn(calendar).anyTimes();
		EasyMock.replay(analyticsReportValue);
		return analyticsReportValue;
	}
}
