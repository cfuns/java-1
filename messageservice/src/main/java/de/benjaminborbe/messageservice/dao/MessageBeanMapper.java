package de.benjaminborbe.messageservice.dao;

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
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

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
		result.add(new StringObjectMapperBase<MessageBean, MessageIdentifier>("id", mapperMessageIdentifier));
		result.add(new StringObjectMapperBase<MessageBean, String>("content", mapperString));
		result.add(new StringObjectMapperBase<MessageBean, String>(TYPE, mapperString));
		result.add(new StringObjectMapperBase<MessageBean, Long>("retryCounter", mapperLong));
		result.add(new StringObjectMapperBase<MessageBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperBase<MessageBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
