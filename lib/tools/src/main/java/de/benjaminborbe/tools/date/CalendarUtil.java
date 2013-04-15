package de.benjaminborbe.tools.date;

import de.benjaminborbe.tools.util.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public interface CalendarUtil {

	Calendar addDays(Calendar now, int amountOfDays);

	Calendar addDays(Calendar calendar, long amountOfDays);

	Calendar addHours(Calendar calendar, int amountOfHours);

	Calendar addMonths(Calendar calendar, int amountOfMonths);

	Calendar addWeeks(Calendar calendar, int amountOfWeeks);

	Calendar clone(Calendar calendar);

	boolean dayEquals(Calendar calendar1, Calendar calendar2);

	Calendar getCalendar(long timeInMillis);

	Calendar getCalendar(TimeZone timeZone, int year, int month, int date);

	Calendar getCalendar(TimeZone timeZone, int year, int month, int date, int hourOfDay, int minute, int second);

	Calendar getCalendar(TimeZone timeZone, int year, int month, int date, int hourOfDay, int minute, int second, int millisecond);

	Calendar getCalendar(TimeZone timeZone, long time);

	long getTime();

	long getTime(Calendar calendar);

	String getWeekday(Calendar calendar);

	boolean isEQ(Calendar c1, Calendar c2);

	boolean isGE(Calendar c1, Calendar c2);

	boolean isGT(Calendar c1, Calendar c2);

	boolean isLE(Calendar c1, Calendar c2);

	boolean isLT(Calendar c1, Calendar c2);

	Calendar max(Calendar c1, Calendar c2);

	Calendar min(Calendar c1, Calendar c2);

	Calendar now();

	Calendar now(TimeZone timeZone);

	Calendar onlyDay(Calendar calendar);

	Calendar parseDate(TimeZone timeZone, Date date) throws ParseException;

	Calendar parseDate(TimeZone timeZone, Date date, Calendar defaultCalendar);

	Calendar parseDate(TimeZone timeZone, String dateString) throws ParseException;

	Calendar parseDateTime(final TimeZone timeZone, String dateTimeString) throws ParseException;

	Calendar parseSmart(Calendar oldValue, String input) throws ParseException;

	Calendar parseSmart(String input) throws ParseException;

	Calendar parseSmart(TimeZone timeZone, Calendar oldValue, String input) throws ParseException;

	Calendar parseSmart(TimeZone timeZone, String input) throws ParseException;

	Calendar parseTimestamp(TimeZone timeZone, String timestamp) throws ParseException;

	Calendar parseTimestamp(TimeZone timeZone, String timestamp, Calendar defaultCalendar);

	Calendar subDays(Calendar now, int amountOfDays);

	String toDateString(Calendar calendar);

	String toDateTimeString(Calendar calendar);

	String toDateTimeZoneString(Calendar calendar);

	Calendar today();

	Calendar today(TimeZone timeZone);

	String toTimeString(Calendar calendar);

	Calendar toTimeZone(Calendar calendar, TimeZone timeZone);

	Calendar getCalendar(Date date);
}
