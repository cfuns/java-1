package de.benjaminborbe.tools.mapper;

import java.util.Calendar;

import com.google.inject.Inject;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class MapperCalendar implements Mapper<Calendar> {

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public MapperCalendar(final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil, final ParseUtil parseUtil) {
		this.timeZoneUtil = timeZoneUtil;
		this.calendarUtil = calendarUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	public Calendar map(final String timestamp) {
		try {
			return timestamp != null ? calendarUtil.getCalendar(timeZoneUtil.getUTCTimeZone(), parseUtil.parseLong(timestamp)) : null;
		}
		catch (final ParseException e) {
			return null;
		}
	}

	@Override
	public String map(final Calendar calendar) {
		return calendar != null ? String.valueOf(calendarUtil.getTime(calendar)) : null;
	}
}
