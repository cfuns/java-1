package de.benjaminborbe.task.dao;

import com.google.inject.Inject;

import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.util.IdGenerator;
import de.benjaminborbe.tools.util.IdGeneratorLong;

public class TaskIdGenerator implements IdGenerator<TaskIdentifier> {

	private final IdGeneratorLong idGeneratorLong;

	@Inject
	public TaskIdGenerator(final IdGeneratorLong idGeneratorLong) {
		this.idGeneratorLong = idGeneratorLong;
	}

	@Override
	public TaskIdentifier nextId() {
		return new TaskIdentifier(String.valueOf(idGeneratorLong.nextId()));
	}

}
