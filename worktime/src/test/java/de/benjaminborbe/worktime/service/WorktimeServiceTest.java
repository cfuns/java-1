package de.benjaminborbe.worktime.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.TimeZone;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerMock;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.guice.WorktimeModulesMock;

public class WorktimeServiceTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		final WorktimeService a = injector.getInstance(WorktimeService.class);
		final WorktimeService b = injector.getInstance(WorktimeService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void getLastDays() {
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
		final WorktimeServiceImpl worktimeService = new WorktimeServiceImpl(null, null, calendarUtil, timeZoneUtil, threadRunner);
		for (int i = 0; i <= 5; ++i) {
			assertNotNull(worktimeService.getLastDays(i));
			assertEquals(i, worktimeService.getLastDays(i).size());
		}
		assertEquals(now0, worktimeService.getLastDays(1).iterator().next());
	}
}
