package de.benjaminborbe.task.core.util;

import com.google.inject.Inject;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.tools.mapper.MapperEnum;
import de.benjaminborbe.tools.util.ParseUtil;

public class MapperTaskFocus extends MapperEnum<TaskFocus> {

	@Inject
	public MapperTaskFocus(final ParseUtil parseUtil) {
		super(parseUtil);
	}

	@Override
	protected Class<TaskFocus> getEnumClass() {
		return TaskFocus.class;
	}

}
