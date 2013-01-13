package de.benjaminborbe.analytics.util;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.tools.iterator.IteratorByList;

public class AnalyticsReportValueIteratorFillMissingValuesUnitTest {

	private final class I extends IteratorByList<AnalyticsReportValue, AnalyticsServiceException> implements AnalyticsReportValueIterator {

		public I(final AnalyticsReportValue[] values) {
			super(values);
		}
	}

	@Test
	public void testIterator() throws Exception {
		final Double v = new Double(1);
		final AnalyticsReportValueIterator analyticsReportValueIterator = buildIterator(buildAnalyticsReportValue(2012, v), buildAnalyticsReportValue(2009, v));
		assertTrue(analyticsReportValueIterator.hasNext());
		assertEquals(2012, analyticsReportValueIterator.next().getDate().get(Calendar.YEAR));
		assertTrue(analyticsReportValueIterator.hasNext());
		assertEquals(2009, analyticsReportValueIterator.next().getDate().get(Calendar.YEAR));
		assertFalse(analyticsReportValueIterator.hasNext());
	}

	@Test
	public void testFill() throws Exception {
		final Double v = new Double(1);
		final AnalyticsReportInterval analyticsReportInterval = AnalyticsReportInterval.YEAR;
		final AnalyticsIntervalUtil analyticsIntervalUtil = new AnalyticsIntervalUtil();
		final AnalyticsReportValueIterator analyticsReportValueIterator = buildIterator(buildAnalyticsReportValue(2012, v), buildAnalyticsReportValue(2009, v));
		final AnalyticsReportValueIteratorFillMissingValues i = new AnalyticsReportValueIteratorFillMissingValues(analyticsIntervalUtil, analyticsReportValueIterator,
				analyticsReportInterval);

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

	private AnalyticsReportValueIterator buildIterator(final AnalyticsReportValue... values) {
		return new I(values);
	}

	private AnalyticsReportValue buildAnalyticsReportValue(final int year, final Double value) {
		final Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);

		final AnalyticsReportValue analyticsReportValue = EasyMock.createMock(AnalyticsReportValue.class);
		EasyMock.expect(analyticsReportValue.getValue()).andReturn(value).anyTimes();
		EasyMock.expect(analyticsReportValue.getDate()).andReturn(calendar).anyTimes();
		EasyMock.replay(analyticsReportValue);
		return analyticsReportValue;
	}
}
