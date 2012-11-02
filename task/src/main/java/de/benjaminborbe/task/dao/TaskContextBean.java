package de.benjaminborbe.task.dao;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;

public class TaskContextBean implements Entity<TaskContextIdentifier>, TaskContext {

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

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(final Calendar created) {
		this.created = created;
	}

	public Calendar getModified() {
		return modified;
	}

	public void setModified(final Calendar modified) {
		this.modified = modified;
	}
}
