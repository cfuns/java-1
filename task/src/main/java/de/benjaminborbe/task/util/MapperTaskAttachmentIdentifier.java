package de.benjaminborbe.task.util;

import de.benjaminborbe.task.api.TaskAttachmentIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperTaskAttachmentIdentifier implements Mapper<TaskAttachmentIdentifier> {

	@Override
	public String toString(final TaskAttachmentIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public TaskAttachmentIdentifier fromString(final String value) {
		return value != null ? new TaskAttachmentIdentifier(value) : null;
	}

}
