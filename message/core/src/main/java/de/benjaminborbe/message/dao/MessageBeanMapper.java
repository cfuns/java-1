package de.benjaminborbe.message.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.message.api.MessageIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class MessageBeanMapper extends MapObjectMapperAdapter<MessageBean> {

	public static final String MODIFIED = "modified";

	public static final String CREATED = "created";

	public static final String START_TIME = "startTime";

	public static final String LOCK_TIME = "lockTime";

	public static final String LOCK_NAME = "lockName";

	public static final String RETRY_COUNTER = "retryCounter";

	public static final String CONTENT = "content";

	public static final String ID = "id";

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
		result.add(new StringObjectMapperAdapter<MessageBean, MessageIdentifier>(ID, mapperMessageIdentifier));
		result.add(new StringObjectMapperAdapter<MessageBean, String>(CONTENT, mapperString));
		result.add(new StringObjectMapperAdapter<MessageBean, String>(TYPE, mapperString));
		result.add(new StringObjectMapperAdapter<MessageBean, Long>(RETRY_COUNTER, mapperLong));
		result.add(new StringObjectMapperAdapter<MessageBean, Long>("maxRetryCounter", mapperLong));
		result.add(new StringObjectMapperAdapter<MessageBean, String>(LOCK_NAME, mapperString));
		result.add(new StringObjectMapperAdapter<MessageBean, Calendar>(LOCK_TIME, mapperCalendar));
		result.add(new StringObjectMapperAdapter<MessageBean, Calendar>(START_TIME, mapperCalendar));
		result.add(new StringObjectMapperAdapter<MessageBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<MessageBean, Calendar>(MODIFIED, mapperCalendar));
		return result;
	}
}
