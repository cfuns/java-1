package de.benjaminborbe.task.core.dao.context;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.core.util.MapperTaskContextIdentifier;
import de.benjaminborbe.task.core.util.MapperUserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class TaskContextBeanMapper extends MapObjectMapperAdapter<TaskContextBean> {

	public static final String OWNER = "owner";

	public static final String NAME = "name";

	@Inject
	public TaskContextBeanMapper(
		final Provider<TaskContextBean> provider,
		final MapperTaskContextIdentifier mapperTaskContextIdentifier,
		final MapperString mapperString,
		final MapperUserIdentifier mapperUserIdentifier,
		final MapperCalendar mapperCalendar
	) {
		super(provider, buildMappings(mapperTaskContextIdentifier, mapperString, mapperUserIdentifier, mapperCalendar));
	}

	private static Collection<StringObjectMapper<TaskContextBean>> buildMappings(
		final MapperTaskContextIdentifier mapperTaskContextIdentifier, final MapperString mapperString,
		final MapperUserIdentifier mapperUserIdentifier, final MapperCalendar mapperCalendar
	) {
		final List<StringObjectMapper<TaskContextBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<TaskContextBean, TaskContextIdentifier>("id", mapperTaskContextIdentifier));
		result.add(new StringObjectMapperAdapter<TaskContextBean, String>(NAME, mapperString));
		result.add(new StringObjectMapperAdapter<TaskContextBean, UserIdentifier>(OWNER, mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<TaskContextBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<TaskContextBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
