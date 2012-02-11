package de.benjaminborbe.tools.date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;
import de.benjaminborbe.tools.util.ParseException;

public class DateUtilTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final DateUtil a = injector.getInstance(DateUtil.class);
		final DateUtil b = injector.getInstance(DateUtil.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void dateString() {
		final DateUtil dateUtil = new DateUtilImpl();
		assertEquals("2011-12-24", dateUtil.dateString(createDate()));
	}

	protected Date createDate() {
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2011);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH, 24);
		c.set(Calendar.HOUR_OF_DAY, 20);
		c.set(Calendar.MINUTE, 15);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	@Test
	public void timeString() {
		final DateUtil dateUtil = new DateUtilImpl();
		assertEquals("20:15:59", dateUtil.timeString(createDate()));
	}

	@Test
	public void dateTimeString() {
		final DateUtil dateUtil = new DateUtilImpl();
		assertEquals("2011-12-24 20:15:59", dateUtil.dateTimeString(createDate()));
	}

	@Test
	public void testParseDateTime() throws Exception {
		final DateUtil dateUtil = new DateUtilImpl();
		try {
			dateUtil.parseDateTime(null);
			fail("exception expected");
		}
		catch (final ParseException e) {
		}
		try {
			dateUtil.parseDateTime("");
			fail("exception expected");
		}
		catch (final ParseException e) {
		}
		try {
			dateUtil.parseDateTime("asdf");
			fail("exception expected");
		}
		catch (final ParseException e) {
		}
		assertEquals(createDate(), dateUtil.parseDateTime("2011-12-24 20:15:59"));
		assertEquals(createDate().getTime(), dateUtil.parseDateTime("2011-12-24 20:15:59").getTime());
	}
}
