package de.benjaminborbe.worktime.core.service;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerMock;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WorktimeServiceUnitTest {

	@Test
	public void testgetLastDays() {
		final TimeZone utcTimeZone = EasyMock.createMock(TimeZone.class);
		EasyMock.replay(utcTimeZone);

		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.expect(timeZoneUtil.getUTCTimeZone()).andReturn(utcTimeZone).anyTimes();
		EasyMock.replay(timeZoneUtil);

		final Calendar now0 = EasyMock.createMock(Calendar.class);
		EasyMock.replay(now0);
		final Calendar now1 = EasyMock.createMock(Calendar.class);
		EasyMock.replay(now1);
		final Calendar now2 = EasyMock.createMock(Calendar.class);
		EasyMock.replay(now2);
		final Calendar now3 = EasyMock.createMock(Calendar.class);
		EasyMock.replay(now3);
		final Calendar now4 = EasyMock.createMock(Calendar.class);
		EasyMock.replay(now4);

		final CalendarUtil calendarUtil = EasyMock.createMock(CalendarUtil.class);
		EasyMock.expect(calendarUtil.now(utcTimeZone)).andReturn(now0).anyTimes();
		EasyMock.expect(calendarUtil.subDays(now0, 0)).andReturn(now0).anyTimes();
		EasyMock.expect(calendarUtil.subDays(now0, 1)).andReturn(now1).anyTimes();
		EasyMock.expect(calendarUtil.subDays(now0, 2)).andReturn(now2).anyTimes();
		EasyMock.expect(calendarUtil.subDays(now0, 3)).andReturn(now3).anyTimes();
		EasyMock.expect(calendarUtil.subDays(now0, 4)).andReturn(now4).anyTimes();
		EasyMock.replay(calendarUtil);

		final ThreadRunner threadRunner = new ThreadRunnerMock();
		final WorktimeServiceImpl worktimeService = new WorktimeServiceImpl(null, null, calendarUtil, timeZoneUtil, threadRunner, null);
		for (int i = 0; i <= 5; ++i) {
			assertNotNull(worktimeService.getLastDays(i));
			assertEquals(i, worktimeService.getLastDays(i).size());
		}
		assertEquals(now0, worktimeService.getLastDays(1).iterator().next());
	}
}
