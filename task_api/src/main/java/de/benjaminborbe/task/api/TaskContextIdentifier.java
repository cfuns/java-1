package de.benjaminborbe.task.api;

import de.benjaminborbe.api.IdentifierBase;

public class TaskContextIdentifier extends IdentifierBase<String> {

	public TaskContextIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public TaskContextIdentifier(final String id) {
		super(id);
	}

}
