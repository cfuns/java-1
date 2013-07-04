package de.benjaminborbe.projectile.util;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ProjectileMailReportFetcherUnitTest {

	@Test
	public void testName() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final CurrentTime currentTime = new CurrentTimeImpl();
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		final ProjectileMailReportFetcher projectileMailReportFetcher = new ProjectileMailReportFetcher(logger, calendarUtil, timeZoneUtil, parseUtil, null, null, null, null, null);
		final String filename = "SlackReport_201303120109541.csv";

		final Calendar date = projectileMailReportFetcher.filenameToCalendar(filename);
		assertThat(date, is(not(nullValue())));
		assertThat(date.get(Calendar.YEAR), is(2013));
		assertThat(date.get(Calendar.MONTH), is(Calendar.MARCH));
		assertThat(date.get(Calendar.DAY_OF_MONTH), is(12));
		assertThat(date.get(Calendar.HOUR_OF_DAY), is(1));
		assertThat(date.get(Calendar.MINUTE), is(9));
		assertThat(date.get(Calendar.SECOND), is(54));
	}
}
