package de.benjaminborbe.tools.date;

import java.util.Calendar;
import java.util.TimeZone;

import de.benjaminborbe.tools.util.ParseException;

public interface CalendarUtil {

	String toDateString(Calendar calendar);

	String toTimeString(Calendar calendar);

	String toDateTimeString(Calendar date);

	Calendar getCalendar(TimeZone timeZone, int year, int month, int date, int hourOfDay, int minute, int second);

	Calendar parseDateTime(final TimeZone timeZone, String dateTimeString) throws ParseException;

	Calendar now();

	Calendar now(TimeZone timeZone);

	Calendar addDays(Calendar now, int amountOfDays);

	Calendar subDays(Calendar now, int amountOfDays);

	Calendar clone(Calendar calendar);

	long getTime(Calendar calendar);

	long getTime();

	String getWeekday(Calendar calendar);

	boolean dayEquals(Calendar calendar1, Calendar calendar2);

	Calendar parseDate(TimeZone timeZone, String dateString) throws ParseException;

	Calendar getCalendar(TimeZone timeZone, int year, int month, int date);

	Calendar getCalendar(TimeZone timeZone, int year, int month, int date, int hourOfDay, int minute, int second, int millisecond);

}
