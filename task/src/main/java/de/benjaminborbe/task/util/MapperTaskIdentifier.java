package de.benjaminborbe.task.util;

import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperTaskIdentifier implements Mapper<TaskIdentifier> {

	@Override
	public String toString(final TaskIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public TaskIdentifier fromString(final String value) {
		return value != null ? new TaskIdentifier(value) : null;
	}

}
