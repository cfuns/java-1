package de.benjaminborbe.task.core.dao.context;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.task.api.TaskContextIdentifier;

public class TaskContextIdentifierBuilder implements IdentifierBuilder<String, TaskContextIdentifier> {

	@Override
	public TaskContextIdentifier buildIdentifier(final String value) {
		return new TaskContextIdentifier(value);
	}

}
