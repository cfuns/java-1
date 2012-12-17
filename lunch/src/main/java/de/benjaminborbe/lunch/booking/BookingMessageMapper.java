package de.benjaminborbe.lunch.booking;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.inject.Inject;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseUtil;

public class BookingMessageMapper {

	private final CalendarUtil calendarUtil;

	private final TimeZone timeZone;

	private final ParseUtil parseUtil;

	@Inject
	public BookingMessageMapper(final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil, final ParseUtil parseUtil) {
		this.calendarUtil = calendarUtil;
		this.parseUtil = parseUtil;
		timeZone = timeZoneUtil.getUTCTimeZone();
	}

	@SuppressWarnings("unchecked")
	public String map(final BookingMessage bookingMessage) throws MapException {
		try {
			final JSONObject object = new JSONObject();
			object.put("user", bookingMessage.getUser());
			object.put("date", bookingMessage.getDate() != null ? String.valueOf(calendarUtil.getTime(bookingMessage.getDate())) : null);
			return object.toJSONString();
		}
		catch (final Exception e) {
			throw new MapException(e);
		}
	}

	public BookingMessage map(final String message) throws MapException {
		try {
			final JSONParser parser = new JSONParser();
			final Object object = parser.parse(message);
			if (object instanceof JSONObject) {
				final JSONObject jsonobject = (JSONObject) object;
				final String user = jsonobject.get("user") != null ? String.valueOf(jsonobject.get("user")) : null;
				Calendar date;
				try {
					date = calendarUtil.getCalendar(timeZone, parseUtil.parseLong(String.valueOf(jsonobject.get("date"))));
				}
				catch (final de.benjaminborbe.tools.util.ParseException e) {
					date = null;
				}
				return new BookingMessage(user, date);
			}
			throw new MapException("not a json object");
		}
		catch (final ParseException e) {
			throw new MapException(e);
		}
	}
}
