package de.benjaminborbe.task.util;

import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperTaskContextIdentifier implements Mapper<TaskContextIdentifier> {

	@Override
	public String toString(final TaskContextIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public TaskContextIdentifier fromString(final String value) {
		return value != null ? new TaskContextIdentifier(value) : null;
	}

}
