package de.benjaminborbe.tools.date;

import de.benjaminborbe.tools.util.ParseException;

import java.util.Date;

public interface DateUtil {

	boolean isToday(Date date);

	String dateString(Date date);

	String timeString(Date date);

	String dateTimeString(Date date);

	Date parseDateTime(String datetime) throws ParseException;

	Date parseDate(String date) throws ParseException;

	String germanDateString(Date date);

	Date today();

}
