package de.benjaminborbe.tools.date;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class CalendarUtilImplIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final CalendarUtil a = injector.getInstance(CalendarUtil.class);
		final CalendarUtil b = injector.getInstance(CalendarUtil.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testToTimeZone() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);
		final TimeZoneUtil timeZoneUtil = injector.getInstance(TimeZoneUtil.class);

		{
			final Calendar calendarUtc = calendarUtil.getCalendar(timeZoneUtil.getUTCTimeZone(), 1357330097000l);
			final Calendar calendar = calendarUtil.toTimeZone(calendarUtc, timeZoneUtil.getEuropeBerlinTimeZone());
			assertEquals(1357330061000l, calendar.getTimeInMillis());
		}

		{
			final Calendar calendarUtc = calendarUtil.getCalendar(timeZoneUtil.getUTCTimeZone(), 1357330097000l);
			final Calendar calendar = calendarUtil.toTimeZone(calendarUtc, timeZoneUtil.getUTCTimeZone());
			assertEquals(1357330097000l, calendar.getTimeInMillis());
		}

		{
			final Calendar calendarUtc = calendarUtil.getCalendar(timeZoneUtil.getEuropeBerlinTimeZone(), 1357330097000l);
			final Calendar calendar = calendarUtil.toTimeZone(calendarUtc, timeZoneUtil.getEuropeBerlinTimeZone());
			assertEquals(1357330097000l, calendar.getTimeInMillis());
		}
	}
}
