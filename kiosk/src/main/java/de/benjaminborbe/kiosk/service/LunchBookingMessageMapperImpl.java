package de.benjaminborbe.kiosk.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.json.JsonObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

public class LunchBookingMessageMapperImpl implements LunchBookingMessageMapper {

	private final JsonObjectMapper<LunchBookingMessage> mapper;

	@Inject
	public LunchBookingMessageMapperImpl(final Provider<LunchBookingMessage> lunchBookingMessageProvider, final MapperCalendar mapperCalendar, final MapperLong mapperLong) {
		mapper = new JsonObjectMapper<LunchBookingMessage>(lunchBookingMessageProvider, build(mapperCalendar, mapperLong));
	}

	private Collection<StringObjectMapper<LunchBookingMessage>> build(final MapperCalendar mapperCalendar, final MapperLong mapperLong) {
		final List<StringObjectMapper<LunchBookingMessage>> result = new ArrayList<StringObjectMapper<LunchBookingMessage>>();
		result.add(new StringObjectMapperAdapter<LunchBookingMessage, Long>(CUSTOMER, mapperLong));
		result.add(new StringObjectMapperAdapter<LunchBookingMessage, Long>(EAN, mapperLong));
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
