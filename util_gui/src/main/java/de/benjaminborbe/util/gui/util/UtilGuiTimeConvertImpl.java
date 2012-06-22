package de.benjaminborbe.util.gui.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.google.inject.Inject;

public class UtilGuiTimeConvertImpl implements UtilGuiTimeConvert {

	@Inject
	public UtilGuiTimeConvertImpl() {
	}

	@Override
	public Calendar convert(final Calendar calendar, final TimeZone timeZone) {
		final Calendar ret = new GregorianCalendar(timeZone);
		ret.setTimeInMillis(calendar.getTimeInMillis() + timeZone.getOffset(calendar.getTimeInMillis()) - TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));
		ret.getTime();
		return ret;
	}

}
