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

	private static final long DAY_MILLISECONDS = 24l * 60l * 60l * 1000l;

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
	public Calendar getCalendar(final TimeZone timeZone, final int year, final int month, final int date, final int hourOfDay, final int minute, final int second) {
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
		try {
			final String[] parts = dateTime.split(" ");
			final String[] dateParts = parts[0].split("-");
			final String[] hourParts = parts[1].split(":");
			return getCalendar(timeZone, parseUtil.parseInt(dateParts[0]), parseUtil.parseInt(dateParts[1]), parseUtil.parseInt(dateParts[2]), parseUtil.parseInt(hourParts[0]),
					parseUtil.parseInt(hourParts[1]), parseUtil.parseInt(hourParts[2]));
		}
		catch (final NullPointerException e) {
			throw new ParseException("NullPointerException while parseing timeZone " + (timeZone != null ? timeZone.getID() : "null") + " dateTime: " + dateTime);
		}
	}

	@Override
	public Calendar now(final TimeZone timeZone) {
		return Calendar.getInstance(timeZone);
	}

	@Override
	public Calendar clone(final Calendar calendar) {
		return (Calendar) calendar.clone();
	}

	@Override
	public Calendar addDays(final Calendar calendar, final int amountOfDays) {
		final Calendar result = clone(calendar);
		result.setTimeInMillis(result.getTimeInMillis() + amountOfDays * DAY_MILLISECONDS);
		return result;
	}

	@Override
	public Calendar subDays(final Calendar calendar, final int amountOfDays) {
		return addDays(calendar, amountOfDays * -1);
	}

	@Override
	public long getTime() {
		return System.currentTimeMillis();
	}

	@Override
	public long getTime(final Calendar calendar) {
		return calendar.getTimeInMillis();
	}

	@Override
	public String getWeekday(final Calendar calendar) {
		final int day = calendar.get(Calendar.DAY_OF_WEEK);
		switch (day) {
		case Calendar.MONDAY:
			return "monday";
		case Calendar.TUESDAY:
			return "tuesday";
		case Calendar.WEDNESDAY:
			return "wednesday";
		case Calendar.THURSDAY:
			return "thursday";
		case Calendar.FRIDAY:
			return "friday";
		case Calendar.SATURDAY:
			return "saturday";
		case Calendar.SUNDAY:
			return "sunday";
		default:
			return null;
		}
	}
}
