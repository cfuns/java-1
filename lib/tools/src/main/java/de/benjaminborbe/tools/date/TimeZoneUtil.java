package de.benjaminborbe.tools.date;

import de.benjaminborbe.tools.util.ParseException;

import java.util.TimeZone;

public interface TimeZoneUtil {

	TimeZone getUTCTimeZone();

	TimeZone getEuropeBerlinTimeZone();

	TimeZone parseTimeZone(String timezone) throws ParseException;

}
