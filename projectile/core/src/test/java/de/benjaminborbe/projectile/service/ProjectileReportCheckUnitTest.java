package de.benjaminborbe.projectile.service;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProjectileReportCheckUnitTest {

	@Test
	public void testName() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final long timeInMillis = 1363177859470l;

		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.expect(currentTime.currentTimeMillis()).andReturn(timeInMillis).anyTimes();
		EasyMock.replay(currentTime);
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		final ProjectileReportCheck check = new ProjectileReportCheck(logger, currentTime, null);
		{
			final Calendar calendar = calendarUtil.getCalendar(timeInMillis);
			assertThat(check.getAgeInHours(calendar), is(0l));
		}
		{
			final Calendar calendar = calendarUtil.getCalendar(timeInMillis - 1000 * 60 * 60 * 4);
			assertThat(check.getAgeInHours(calendar), is(4l));
		}
	}
}
