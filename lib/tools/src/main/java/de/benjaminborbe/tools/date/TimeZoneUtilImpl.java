package de.benjaminborbe.tools.date;

import java.util.TimeZone;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.util.ParseException;

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

	@Override
	public TimeZone parseTimeZone(final String timezone) throws ParseException {
		if (timezone == null) {
			throw new ParseException("can't parse timezone: null");
		}
		final TimeZone result = TimeZone.getTimeZone(timezone);
		if (!timezone.equalsIgnoreCase(result.getID())) {
			throw new ParseException("can't parse timezone: null");
		}
		return result;
	}
}
