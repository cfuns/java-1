package de.benjaminborbe.task.api;

import de.benjaminborbe.api.IdentifierBase;

public class TaskIdentifier extends IdentifierBase<String> {

	public TaskIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public TaskIdentifier(final String id) {
		super(id);
	}

}
