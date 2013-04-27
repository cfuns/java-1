package de.benjaminborbe.kiosk.util;

import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.json.JsonObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KioskBookingMessageMapperImpl implements KioskBookingMessageMapper {

	private final JsonObjectMapper<KioskBookingMessage> mapper;

	@Inject
	public KioskBookingMessageMapperImpl(final Provider<KioskBookingMessage> lunchBookingMessageProvider, final MapperLong mapperLong) {
		mapper = new JsonObjectMapper<>(lunchBookingMessageProvider, build(mapperLong));
	}

	private Collection<StringObjectMapper<KioskBookingMessage>> build(final MapperLong mapperLong) {
		final List<StringObjectMapper<KioskBookingMessage>> result = new ArrayList<>();
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
