package de.benjaminborbe.task.dao.attachment;

import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.task.api.TaskAttachment;
import de.benjaminborbe.task.api.TaskAttachmentIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;

import java.util.Calendar;

public class TaskAttachmentBean extends EntityBase<TaskAttachmentIdentifier> implements TaskAttachment, HasCreated, HasModified {

	private static final long serialVersionUID = 6058606350883201939L;

	private TaskAttachmentIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	public void setTask(final TaskIdentifier task) {
		this.task = task;
	}

	private TaskIdentifier task;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TaskIdentifier getTask() {
		return task;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public TaskAttachmentIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final TaskAttachmentIdentifier id) {
		this.id = id;
	}

}
