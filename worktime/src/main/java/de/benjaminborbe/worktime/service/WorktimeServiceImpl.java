package de.benjaminborbe.worktime.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.worktime.api.Workday;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.api.WorktimeServiceException;
import de.benjaminborbe.worktime.util.InOfficeCheckHttpContent;
import de.benjaminborbe.worktime.util.WorkdayImpl;
import de.benjaminborbe.worktime.util.WorktimeStorageService;
import de.benjaminborbe.worktime.util.WorktimeValue;

@Singleton
public class WorktimeServiceImpl implements WorktimeService {

	private final class WorkdayCalcRunnable implements Runnable {

		private final Calendar calendar;

		private final Set<Workday> workdays;

		private WorkdayCalcRunnable(final Calendar calendar, final Set<Workday> workdays) {
			this.calendar = calendar;
			this.workdays = workdays;
		}

		@Override
		public void run() {
			try {
				workdays.add(getWorkday(calendar));
			}
			catch (final Exception e) {
				logger.error(e.getClass().getSimpleName(), e);
			}
		}
	}

	private final class WorkdayComparator implements Comparator<Workday> {

		@Override
		public int compare(final Workday w1, final Workday w2) {
			return w1.getDate().compareTo(w2.getDate());
		}
	}

	private final Logger logger;

	private final WorktimeStorageService worktimeStorageService;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final ThreadRunner threadRunner;

	private final InOfficeCheckHttpContent inOfficeCheck;

	@Inject
	public WorktimeServiceImpl(
			final Logger logger,
			final WorktimeStorageService worktimeStorageService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ThreadRunner threadRunner,
			final InOfficeCheckHttpContent inOfficeCheck) {
		this.logger = logger;
		this.worktimeStorageService = worktimeStorageService;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.threadRunner = threadRunner;
		this.inOfficeCheck = inOfficeCheck;
	}

	@Override
	public List<Workday> getTimes(final int days) {
		logger.trace("get times for " + days + " days");
		final Set<Workday> workdays = new HashSet<Workday>();
		final Collection<Calendar> calendars = getLastDays(days);

		final Set<Thread> threads = new HashSet<Thread>();
		for (final Calendar calendar : calendars) {
			threads.add(threadRunner.run("workday-calc", new WorkdayCalcRunnable(calendar, workdays)));
		}
		for (final Thread thread : threads) {
			try {
				thread.join();
			}
			catch (final InterruptedException e) {
			}
		}
		final List<Workday> result = new ArrayList<Workday>(workdays);
		Collections.sort(result, new WorkdayComparator());
		return result;
	}

	protected Workday getWorkday(final Calendar calendar) throws StorageException {
		// add cache be a good idea
		final Collection<WorktimeValue> workTimeValues = worktimeStorageService.findByDate(calendar);
		Calendar first = null;
		Calendar last = null;
		for (final WorktimeValue workTimeValue : workTimeValues) {
			if (first == null || (first.after(workTimeValue.getDate()) && workTimeValue.getInOffice()))
				first = workTimeValue.getDate();
			if (last == null || (last.before(workTimeValue.getDate()) && workTimeValue.getInOffice()))
				last = workTimeValue.getDate();
		}
		return new WorkdayImpl(calendar, first, last);
	}

	protected Collection<Calendar> getLastDays(final int amount) {
		final List<Calendar> calendars = new ArrayList<Calendar>();
		final Calendar now = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		for (int i = 0; i < amount; ++i) {
			calendars.add(calendarUtil.subDays(now, i));
		}
		return calendars;
	}

	@Override
	public boolean isOffice() throws WorktimeServiceException {
		logger.trace("isOffice");
		return inOfficeCheck.check();
	}
}
