package de.benjaminborbe.task.core.dao.task;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.task.api.TaskIdentifier;

public class TaskIdentifierBuilder implements IdentifierBuilder<String, TaskIdentifier> {

	@Override
	public TaskIdentifier buildIdentifier(final String value) {
		return new TaskIdentifier(value);
	}

}
