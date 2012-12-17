package de.benjaminborbe.task.dao;

import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperTaskIdentifier<B> extends StringObjectMapperBase<B, TaskIdentifier> {

	public StringObjectMapperTaskIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final TaskIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public TaskIdentifier fromString(final String value) {
		return value != null ? new TaskIdentifier(value) : null;
	}

}
