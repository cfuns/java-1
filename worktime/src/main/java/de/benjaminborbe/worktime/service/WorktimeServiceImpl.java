package de.benjaminborbe.worktime.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.worktime.api.Workday;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.util.WorkdayImpl;
import de.benjaminborbe.worktime.util.WorktimeStorageService;
import de.benjaminborbe.worktime.util.WorktimeValue;

@Singleton
public class WorktimeServiceImpl implements WorktimeService {

	private final class CalendarComparator implements Comparator<Calendar> {

		@Override
		public int compare(final Calendar o1, final Calendar o2) {
			return o1.compareTo(o2);
		}
	}

	private final Logger logger;

	private final WorktimeStorageService worktimeStorageService;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public WorktimeServiceImpl(final Logger logger, final WorktimeStorageService worktimeStorageService, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil) {
		this.logger = logger;
		this.worktimeStorageService = worktimeStorageService;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
	}

	@Override
	public List<Workday> getTimes(final int days) throws StorageException {
		logger.debug("get times for " + days + " days");
		final List<Workday> result = new ArrayList<Workday>();

		final List<Calendar> calendars = getLastDays(days);
		Collections.sort(calendars, new CalendarComparator());
		for (final Calendar date : calendars) {
			final Collection<WorktimeValue> workTimeValues = worktimeStorageService.findByDate(date);
			Calendar first = null;
			Calendar last = null;
			for (final WorktimeValue workTimeValue : workTimeValues) {
				if (first == null || (first.after(workTimeValue.getDate()) && workTimeValue.getInOffice()))
					first = workTimeValue.getDate();
				if (last == null || (last.before(workTimeValue.getDate()) && workTimeValue.getInOffice()))
					last = workTimeValue.getDate();
			}
			result.add(new WorkdayImpl(date, first, last));
		}
		return result;
	}

	protected List<Calendar> getLastDays(final int amount) {
		final List<Calendar> calendars = new ArrayList<Calendar>();
		final Calendar now = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		for (int i = 0; i < amount; ++i) {
			calendars.add(calendarUtil.subDays(now, i));
		}
		return calendars;
	}
}
