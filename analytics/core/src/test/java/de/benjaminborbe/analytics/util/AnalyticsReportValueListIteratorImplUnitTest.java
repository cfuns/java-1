package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.tools.iterator.IteratorByList;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AnalyticsReportValueListIteratorImplUnitTest {

	private final class I extends IteratorByList<AnalyticsReportValue, AnalyticsServiceException> implements AnalyticsReportValueIterator {

		public I(final AnalyticsReportValue[] values) {
			super(values);
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
		EasyMock.expect(analyticsReportValue.getCounter()).andReturn(1l).anyTimes();
		EasyMock.expect(analyticsReportValue.getValue()).andReturn(value).anyTimes();
		EasyMock.expect(analyticsReportValue.getDate()).andReturn(calendar).anyTimes();
		EasyMock.replay(analyticsReportValue);
		return analyticsReportValue;
	}

	@Test
	public void testEmpty() throws Exception {
		final AnalyticsReportValueListIteratorImpl iterator = new AnalyticsReportValueListIteratorImpl(new ArrayList<AnalyticsReportValueIterator>());
		assertFalse(iterator.hasNext());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testOneIteratorOneValue() throws Exception {
		final AnalyticsReportValueIterator i1 = buildIterator(buildAnalyticsReportValue(1, 1d));
		final List<AnalyticsReportValueIterator> is = Arrays.asList(i1);
		final AnalyticsReportValueListIteratorImpl iterator = new AnalyticsReportValueListIteratorImpl(is);
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(1, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(1d), values.get(0).getValue());
		}
		{
			assertFalse(iterator.hasNext());
			assertFalse(iterator.hasNext());
		}
	}

	@Test
	public void testOneIteratorManyValues() throws Exception {
		final AnalyticsReportValueIterator i1 = buildIterator(buildAnalyticsReportValue(1, 1d), buildAnalyticsReportValue(2, 2d), buildAnalyticsReportValue(3, 3d));
		final List<AnalyticsReportValueIterator> is = Arrays.asList(i1);
		final AnalyticsReportValueListIteratorImpl iterator = new AnalyticsReportValueListIteratorImpl(is);
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(1, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(1d), values.get(0).getValue());
		}
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(2, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(2d), values.get(0).getValue());
		}
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(3, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(3d), values.get(0).getValue());
		}
		{
			assertFalse(iterator.hasNext());
			assertFalse(iterator.hasNext());
		}
	}

	@Test
	public void testManyIteratorsOneValue() throws Exception {
		final AnalyticsReportValueIterator i1 = buildIterator(buildAnalyticsReportValue(1, 1d));
		final AnalyticsReportValueIterator i2 = buildIterator(buildAnalyticsReportValue(1, 2d));
		final AnalyticsReportValueIterator i3 = buildIterator(buildAnalyticsReportValue(1, 3d));
		final List<AnalyticsReportValueIterator> is = Arrays.asList(i1, i2, i3);
		final AnalyticsReportValueListIteratorImpl iterator = new AnalyticsReportValueListIteratorImpl(is);
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(1, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(1d), values.get(0).getValue());
			assertEquals(1, values.get(1).getDate().get(Calendar.YEAR));
			assertEquals(new Double(2d), values.get(1).getValue());
			assertEquals(1, values.get(2).getDate().get(Calendar.YEAR));
			assertEquals(new Double(3d), values.get(2).getValue());
		}
		{
			assertFalse(iterator.hasNext());
			assertFalse(iterator.hasNext());
		}
	}

	@Test
	public void testManyIteratorsManyValues() throws Exception {
		final AnalyticsReportValueIterator i1 = buildIterator(buildAnalyticsReportValue(1, 1d), buildAnalyticsReportValue(2, 4d), buildAnalyticsReportValue(3, 7d));
		final AnalyticsReportValueIterator i2 = buildIterator(buildAnalyticsReportValue(1, 2d), buildAnalyticsReportValue(2, 5d), buildAnalyticsReportValue(3, 8d));
		final AnalyticsReportValueIterator i3 = buildIterator(buildAnalyticsReportValue(1, 3d), buildAnalyticsReportValue(2, 6d), buildAnalyticsReportValue(3, 9d));
		final List<AnalyticsReportValueIterator> is = Arrays.asList(i1, i2, i3);
		final AnalyticsReportValueListIteratorImpl iterator = new AnalyticsReportValueListIteratorImpl(is);
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(1, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(1d), values.get(0).getValue());
			assertEquals(1, values.get(1).getDate().get(Calendar.YEAR));
			assertEquals(new Double(2d), values.get(1).getValue());
			assertEquals(1, values.get(2).getDate().get(Calendar.YEAR));
			assertEquals(new Double(3d), values.get(2).getValue());
		}
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(2, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(4d), values.get(0).getValue());
			assertEquals(2, values.get(1).getDate().get(Calendar.YEAR));
			assertEquals(new Double(5d), values.get(1).getValue());
			assertEquals(2, values.get(2).getDate().get(Calendar.YEAR));
			assertEquals(new Double(6d), values.get(2).getValue());
		}
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(3, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(7d), values.get(0).getValue());
			assertEquals(3, values.get(1).getDate().get(Calendar.YEAR));
			assertEquals(new Double(8d), values.get(1).getValue());
			assertEquals(3, values.get(2).getDate().get(Calendar.YEAR));
			assertEquals(new Double(9d), values.get(2).getValue());
		}
		{
			assertFalse(iterator.hasNext());
			assertFalse(iterator.hasNext());
		}
	}

	@Test
	public void testManyIteratorsManyValuesHoles() throws Exception {
		final AnalyticsReportValueIterator i1 = buildIterator(buildAnalyticsReportValue(1, 1d), buildAnalyticsReportValue(2, 4d));
		final AnalyticsReportValueIterator i2 = buildIterator(buildAnalyticsReportValue(1, 2d), buildAnalyticsReportValue(3, 8d));
		final AnalyticsReportValueIterator i3 = buildIterator(buildAnalyticsReportValue(2, 6d), buildAnalyticsReportValue(3, 9d));
		final List<AnalyticsReportValueIterator> is = Arrays.asList(i1, i2, i3);
		final AnalyticsReportValueListIteratorImpl iterator = new AnalyticsReportValueListIteratorImpl(is);
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(1, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(1d), values.get(0).getValue());
			assertEquals(1, values.get(1).getDate().get(Calendar.YEAR));
			assertEquals(new Double(2d), values.get(1).getValue());
			assertNull(values.get(2));
		}
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertEquals(2, values.get(0).getDate().get(Calendar.YEAR));
			assertEquals(new Double(4d), values.get(0).getValue());
			assertNull(values.get(1));
			assertEquals(2, values.get(2).getDate().get(Calendar.YEAR));
			assertEquals(new Double(6d), values.get(2).getValue());
		}
		{
			assertTrue(iterator.hasNext());
			assertTrue(iterator.hasNext());
			final List<AnalyticsReportValue> values = iterator.next();
			assertNotNull(values);
			assertEquals(is.size(), values.size());
			assertNull(values.get(0));
			assertEquals(3, values.get(1).getDate().get(Calendar.YEAR));
			assertEquals(new Double(8d), values.get(1).getValue());
			assertEquals(3, values.get(2).getDate().get(Calendar.YEAR));
			assertEquals(new Double(9d), values.get(2).getValue());
		}
		{
			assertFalse(iterator.hasNext());
			assertFalse(iterator.hasNext());
		}
	}
}
