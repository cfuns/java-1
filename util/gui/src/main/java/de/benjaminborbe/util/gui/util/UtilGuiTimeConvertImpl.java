package de.benjaminborbe.util.gui.util;

import de.benjaminborbe.tools.date.CalendarUtil;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.TimeZone;

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
