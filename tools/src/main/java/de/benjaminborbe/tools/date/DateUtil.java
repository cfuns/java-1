package de.benjaminborbe.tools.date;

import java.util.Date;

import de.benjaminborbe.tools.util.ParseException;

public interface DateUtil {

	String dateString(Date date);

	String timeString(Date date);

	String dateTimeString(Date date);

	Date parseDateTime(String datetime) throws ParseException;

	Date parseDate(String date) throws ParseException;

}
