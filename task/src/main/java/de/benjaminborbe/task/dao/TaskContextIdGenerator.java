package de.benjaminborbe.task.dao;

import com.google.inject.Inject;

import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.tools.util.IdGenerator;
import de.benjaminborbe.tools.util.IdGeneratorLong;

public class TaskContextIdGenerator implements IdGenerator<TaskContextIdentifier> {

	private final IdGeneratorLong idGeneratorLong;

	@Inject
	public TaskContextIdGenerator(final IdGeneratorLong idGeneratorLong) {
		this.idGeneratorLong = idGeneratorLong;
	}

	@Override
	public TaskContextIdentifier nextId() {
		return new TaskContextIdentifier(String.valueOf(idGeneratorLong.nextId()));
	}

}
