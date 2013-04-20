package de.benjaminborbe.util.gui.util;

import java.util.Calendar;
import java.util.TimeZone;

import javax.inject.Inject;

import de.benjaminborbe.tools.date.CalendarUtil;

public class UtilGuiTimeConvertImpl implements UtilGuiTimeConvert {

	private final CalendarUtil calendarUtil;

	@Inject
	public UtilGuiTimeConvertImpl(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Calendar convert(final Calendar calendar, final TimeZone timeZone) {
		return calendarUtil.toTimeZone(calendar, timeZone);
	}

}
