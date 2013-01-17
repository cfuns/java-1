package de.benjaminborbe.kiosk.util;

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

public class KioskBookingMessageMapperImpl implements KioskBookingMessageMapper {

	private final JsonObjectMapper<KioskBookingMessage> mapper;

	@Inject
	public KioskBookingMessageMapperImpl(final Provider<KioskBookingMessage> lunchBookingMessageProvider, final MapperCalendar mapperCalendar, final MapperLong mapperLong) {
		mapper = new JsonObjectMapper<KioskBookingMessage>(lunchBookingMessageProvider, build(mapperCalendar, mapperLong));
	}

	private Collection<StringObjectMapper<KioskBookingMessage>> build(final MapperCalendar mapperCalendar, final MapperLong mapperLong) {
		final List<StringObjectMapper<KioskBookingMessage>> result = new ArrayList<StringObjectMapper<KioskBookingMessage>>();
		result.add(new StringObjectMapperAdapter<KioskBookingMessage, Long>(CUSTOMER, mapperLong));
		result.add(new StringObjectMapperAdapter<KioskBookingMessage, Long>(EAN, mapperLong));
		return result;
	}

	@Override
	public String map(final KioskBookingMessage bookingMessage) throws MapException {
		return mapper.toJson(bookingMessage);
	}

	@Override
	public KioskBookingMessage map(final String message) throws MapException {
		return mapper.fromJson(message);
	}

}
