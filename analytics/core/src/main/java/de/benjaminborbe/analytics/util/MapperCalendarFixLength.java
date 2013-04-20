package de.benjaminborbe.analytics.util;

import java.util.Calendar;

import javax.inject.Inject;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.tools.mapper.MapperCalendar;

public class MapperCalendarFixLength implements Mapper<Calendar> {

	private final MapperCalendar mapperCalendar;

	private final CalendarUtil calendarUtil;

	@Inject
	public MapperCalendarFixLength(final MapperCalendar mapperCalendar, final CalendarUtil calendarUtil) {
		this.mapperCalendar = mapperCalendar;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Calendar fromString(final String timestamp) {
		return mapperCalendar.fromString(timestamp);
	}

	@Override
	public String toString(final Calendar calendar) {
		return String.format("%019d", calendarUtil.getTime(calendar));
	}

}
