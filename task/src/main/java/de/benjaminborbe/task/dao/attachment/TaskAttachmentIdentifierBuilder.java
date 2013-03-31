package de.benjaminborbe.task.dao.attachment;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.task.api.TaskAttachmentIdentifier;

public class TaskAttachmentIdentifierBuilder implements IdentifierBuilder<String, TaskAttachmentIdentifier> {

	@Override
	public TaskAttachmentIdentifier buildIdentifier(final String value) {
		return new TaskAttachmentIdentifier(value);
	}

}
