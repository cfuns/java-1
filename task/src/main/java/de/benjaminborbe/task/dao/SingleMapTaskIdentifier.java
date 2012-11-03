package de.benjaminborbe.task.dao;

import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.mapper.SingleMapBase;

public class SingleMapTaskIdentifier<B> extends SingleMapBase<B, TaskIdentifier> {

	public SingleMapTaskIdentifier(final String name) {
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
