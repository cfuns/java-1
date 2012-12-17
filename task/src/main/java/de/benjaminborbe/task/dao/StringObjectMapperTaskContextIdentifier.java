package de.benjaminborbe.task.dao;

import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

public class StringObjectMapperTaskContextIdentifier<B> extends StringObjectMapperBase<B, TaskContextIdentifier> {

	public StringObjectMapperTaskContextIdentifier(final String name) {
		super(name);
	}

	@Override
	public String toString(final TaskContextIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public TaskContextIdentifier fromString(final String value) {
		return value != null ? new TaskContextIdentifier(value) : null;
	}

}
