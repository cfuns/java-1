package de.benjaminborbe.tools.mapper;

import java.util.Calendar;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class SingleMapCalendar<T> extends SingleMapBase<T, Calendar> {

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	private final TimeZoneUtil timeZoneUtil;

	public SingleMapCalendar(final String name, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil, final ParseUtil parseUtil) {
		super(name);
		this.timeZoneUtil = timeZoneUtil;
		this.calendarUtil = calendarUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	public Calendar fromString(final String timestamp) {
		try {
			return timestamp != null ? calendarUtil.getCalendar(timeZoneUtil.getUTCTimeZone(), parseUtil.parseLong(timestamp)) : null;
		}
		catch (final ParseException e) {
			return null;
		}
	}

	@Override
	public String toString(final Calendar calendar) {
		return calendar != null ? String.valueOf(calendarUtil.getTime(calendar)) : null;
	}

}
