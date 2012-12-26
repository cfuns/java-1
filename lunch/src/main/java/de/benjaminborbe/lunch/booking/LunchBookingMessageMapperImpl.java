package de.benjaminborbe.lunch.booking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.json.JsonObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

public class LunchBookingMessageMapperImpl implements LunchBookingMessageMapper {

	private final JsonObjectMapper<LunchBookingMessage> mapper;

	@Inject
	public LunchBookingMessageMapperImpl(final Provider<LunchBookingMessage> lunchBookingMessageProvider, final MapperCalendar mapperCalendar, final MapperString mapperString) {
		mapper = new JsonObjectMapper<LunchBookingMessage>(lunchBookingMessageProvider, build(mapperCalendar, mapperString));
	}

	private Collection<StringObjectMapper<LunchBookingMessage>> build(final MapperCalendar mapperCalendar, final MapperString mapperString) {
		final List<StringObjectMapper<LunchBookingMessage>> result = new ArrayList<StringObjectMapper<LunchBookingMessage>>();
		result.add(new StringObjectMapperAdapter<LunchBookingMessage, Calendar>(DATE, mapperCalendar));
		result.add(new StringObjectMapperAdapter<LunchBookingMessage, String>(CUSTOMER_NUMBER, mapperString));
		return result;
	}

	@Override
	public String map(final LunchBookingMessage bookingMessage) throws MapException {
		return mapper.toJson(bookingMessage);
	}

	@Override
	public LunchBookingMessage map(final String message) throws MapException {
		return mapper.fromJson(message);
	}

}
