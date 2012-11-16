package de.benjaminborbe.task.dao;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;

public class TaskContextBean implements Entity<TaskContextIdentifier>, TaskContext, HasCreated, HasModified {

	private static final long serialVersionUID = 6058606350883201939L;

	private TaskContextIdentifier id;

	private String name;

	private UserIdentifier owner;

	private Calendar created;

	private Calendar modified;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void setId(final TaskContextIdentifier id) {
		this.id = id;
	}

	@Override
	public TaskContextIdentifier getId() {
		return id;
	}

	@Override
	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
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
}
