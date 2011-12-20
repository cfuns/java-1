package de.benjaminborbe.tools.date;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.TimeZone;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class CalendarUtilImpl implements CalendarUtil {

	private final ParseUtil parseUtil;

	@Inject
	public CalendarUtilImpl(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	@Override
	public String toDateString(final Calendar calendar) {
		final StringWriter sw = new StringWriter();
		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH) + 1;
		final int date = calendar.get(Calendar.DAY_OF_MONTH);
		sw.append(String.valueOf(year));
		sw.append("-");
		if (month < 10)
			sw.append("0");
		sw.append(String.valueOf(month));
		sw.append("-");
		if (date < 10)
			sw.append("0");
		sw.append(String.valueOf(date));
		return sw.toString();
	}

	@Override
	public String toTimeString(final Calendar calendar) {
		final StringWriter sw = new StringWriter();
		final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
		final int second = calendar.get(Calendar.SECOND);
		if (hourOfDay < 10)
			sw.append("0");
		sw.append(String.valueOf(hourOfDay));
		sw.append(":");
		if (minute < 10)
			sw.append("0");
		sw.append(String.valueOf(minute));
		sw.append(":");
		if (second < 10)
			sw.append("0");
		sw.append(String.valueOf(second));
		return sw.toString();
	}

	@Override
	public Calendar getCalendar(final TimeZone timeZone, final int year, final int month, final int date,
			final int hourOfDay, final int minute, final int second) {
		final Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeZone(timeZone);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, date);
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar;
	}

	@Override
	public String toDateTimeString(final Calendar date) {
		return toDateString(date) + " " + toTimeString(date);
	}

	@Override
	public Calendar parseDateTime(final TimeZone timeZone, final String dateTime) throws ParseException {
		final String[] parts = dateTime.split(" ");
		final String[] dateParts = parts[0].split("-");
		final String[] hourParts = parts[1].split(":");
		return getCalendar(timeZone, parseUtil.parseInt(dateParts[0]), parseUtil.parseInt(dateParts[1]),
				parseUtil.parseInt(dateParts[2]), parseUtil.parseInt(hourParts[0]), parseUtil.parseInt(hourParts[1]),
				parseUtil.parseInt(hourParts[2]));
	}
}
