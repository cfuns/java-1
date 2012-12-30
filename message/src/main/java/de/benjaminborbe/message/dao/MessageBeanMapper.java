package de.benjaminborbe.message.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class MessageBeanMapper extends MapObjectMapperAdapter<MessageBean> {

	public static final String TYPE = "type";

	@Inject
	public MessageBeanMapper(
			final Provider<MessageBean> provider,
			final MapperMessageIdentifier mapperMessageIdentifier,
			final MapperString mapperString,
			final MapperCalendar mapperCalendar,
			final MapperLong mapperLong) {
		super(provider, buildMappings(mapperMessageIdentifier, mapperString, mapperCalendar, mapperLong));
	}

	private static Collection<StringObjectMapper<MessageBean>> buildMappings(final MapperMessageIdentifier mapperMessageIdentifier, final MapperString mapperString,
			final MapperCalendar mapperCalendar, final MapperLong mapperLong) {
		final List<StringObjectMapper<MessageBean>> result = new ArrayList<StringObjectMapper<MessageBean>>();
		result.add(new StringObjectMapperAdapter<MessageBean, MessageIdentifier>("id", mapperMessageIdentifier));
		result.add(new StringObjectMapperAdapter<MessageBean, String>("content", mapperString));
		result.add(new StringObjectMapperAdapter<MessageBean, String>(TYPE, mapperString));
		result.add(new StringObjectMapperAdapter<MessageBean, Long>("retryCounter", mapperLong));
		result.add(new StringObjectMapperAdapter<MessageBean, String>("lockName", mapperString));
		result.add(new StringObjectMapperAdapter<MessageBean, Calendar>("lockTime", mapperCalendar));
		result.add(new StringObjectMapperAdapter<MessageBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<MessageBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
