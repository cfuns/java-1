package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class TaskBeanMapper extends MapObjectMapperAdapter<TaskBean> {

	@Inject
	public TaskBeanMapper(
			final Provider<TaskBean> provider,
			final MapperTaskIdentifier mapperTaskIdentifier,
			final MapperString mapperString,
			final MapperUserIdentifier mapperUserIdentifier,
			final MapperLong mapperLong,
			final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar,
			final MapperInteger mapperInteger) {
		super(provider, buildMappings(mapperTaskIdentifier, mapperString, mapperUserIdentifier, mapperLong, mapperBoolean, mapperCalendar, mapperInteger));
	}

	private static Collection<StringObjectMapper<TaskBean>> buildMappings(final MapperTaskIdentifier mapperTaskIdentifier, final MapperString mapperString,
			final MapperUserIdentifier mapperUserIdentifier, final MapperLong mapperLong, final MapperBoolean mapperBoolean, final MapperCalendar mapperCalendar,
			final MapperInteger mapperInteger) {
		final List<StringObjectMapper<TaskBean>> result = new ArrayList<StringObjectMapper<TaskBean>>();
		result.add(new StringObjectMapperBase<TaskBean, TaskIdentifier>("id", mapperTaskIdentifier));
		result.add(new StringObjectMapperBase<TaskBean, TaskIdentifier>("parentId", mapperTaskIdentifier));
		result.add(new StringObjectMapperBase<TaskBean, String>("name", mapperString));
		result.add(new StringObjectMapperBase<TaskBean, String>("description", mapperString));
		result.add(new StringObjectMapperBase<TaskBean, UserIdentifier>("owner", mapperUserIdentifier));
		result.add(new StringObjectMapperBase<TaskBean, Long>("duration", mapperLong));
		result.add(new StringObjectMapperBase<TaskBean, Boolean>("completed", mapperBoolean));
		result.add(new StringObjectMapperBase<TaskBean, Calendar>("completionDate", mapperCalendar));
		result.add(new StringObjectMapperBase<TaskBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperBase<TaskBean, Calendar>("modified", mapperCalendar));
		result.add(new StringObjectMapperBase<TaskBean, Calendar>("start", mapperCalendar));
		result.add(new StringObjectMapperBase<TaskBean, Calendar>("due", mapperCalendar));
		result.add(new StringObjectMapperBase<TaskBean, Integer>("priority", mapperInteger));
		result.add(new StringObjectMapperBase<TaskBean, Long>("repeatStart", mapperLong));
		result.add(new StringObjectMapperBase<TaskBean, Long>("repeatDue", mapperLong));
		result.add(new StringObjectMapperBase<TaskBean, String>("url", mapperString));
		return result;
	}
}
