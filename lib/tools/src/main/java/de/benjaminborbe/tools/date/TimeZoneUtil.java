package de.benjaminborbe.tools.date;

import java.util.TimeZone;

import de.benjaminborbe.tools.util.ParseException;

public interface TimeZoneUtil {

	TimeZone getUTCTimeZone();

	TimeZone getEuropeBerlinTimeZone();

	TimeZone parseTimeZone(String timezone) throws ParseException;

}
