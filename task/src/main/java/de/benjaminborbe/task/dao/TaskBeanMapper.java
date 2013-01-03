package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.util.MapperTaskFocus;
import de.benjaminborbe.task.util.MapperTaskIdentifier;
import de.benjaminborbe.task.util.MapperUserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

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
			final MapperInteger mapperInteger,
			final MapperTaskFocus mapperTaskFocus) {
		super(provider, buildMappings(mapperTaskIdentifier, mapperString, mapperUserIdentifier, mapperLong, mapperBoolean, mapperCalendar, mapperInteger, mapperTaskFocus));
	}

	private static Collection<StringObjectMapper<TaskBean>> buildMappings(final MapperTaskIdentifier mapperTaskIdentifier, final MapperString mapperString,
			final MapperUserIdentifier mapperUserIdentifier, final MapperLong mapperLong, final MapperBoolean mapperBoolean, final MapperCalendar mapperCalendar,
			final MapperInteger mapperInteger, final MapperTaskFocus mapperTaskFocus) {
		final List<StringObjectMapper<TaskBean>> result = new ArrayList<StringObjectMapper<TaskBean>>();
		result.add(new StringObjectMapperAdapter<TaskBean, TaskIdentifier>("id", mapperTaskIdentifier));
		result.add(new StringObjectMapperAdapter<TaskBean, TaskIdentifier>("parentId", mapperTaskIdentifier));
		result.add(new StringObjectMapperAdapter<TaskBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<TaskBean, String>("description", mapperString));
		result.add(new StringObjectMapperAdapter<TaskBean, UserIdentifier>("owner", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<TaskBean, Long>("duration", mapperLong));
		result.add(new StringObjectMapperAdapter<TaskBean, Boolean>("completed", mapperBoolean));
		result.add(new StringObjectMapperAdapter<TaskBean, Calendar>("completionDate", mapperCalendar));
		result.add(new StringObjectMapperAdapter<TaskBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<TaskBean, Calendar>("modified", mapperCalendar));
		result.add(new StringObjectMapperAdapter<TaskBean, Calendar>("start", mapperCalendar));
		result.add(new StringObjectMapperAdapter<TaskBean, Calendar>("due", mapperCalendar));
		result.add(new StringObjectMapperAdapter<TaskBean, Integer>("priority", mapperInteger));
		result.add(new StringObjectMapperAdapter<TaskBean, Long>("repeatStart", mapperLong));
		result.add(new StringObjectMapperAdapter<TaskBean, Long>("repeatDue", mapperLong));
		result.add(new StringObjectMapperAdapter<TaskBean, String>("url", mapperString));
		result.add(new StringObjectMapperAdapter<TaskBean, TaskFocus>("focus", mapperTaskFocus));
		return result;
	}
}
