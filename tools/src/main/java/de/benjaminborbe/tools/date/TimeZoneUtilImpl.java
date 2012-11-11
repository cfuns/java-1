package de.benjaminborbe.tools.date;

import java.util.TimeZone;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TimeZoneUtilImpl implements TimeZoneUtil {

	@Inject
	public TimeZoneUtilImpl() {
	}

	@Override
	public TimeZone getUTCTimeZone() {
		return TimeZone.getTimeZone("UTC");
	}

	@Override
	public TimeZone getEuropeBerlinTimeZone() {
		return TimeZone.getTimeZone("Europe/Berlin");
	}

}
