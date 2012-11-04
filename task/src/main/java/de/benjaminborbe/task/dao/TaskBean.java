package de.benjaminborbe.task.dao;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskIdentifier;

public class TaskBean implements Entity<TaskIdentifier>, Task {

	private static final long serialVersionUID = 6058606350883201939L;

	private Boolean completed;

	private TaskIdentifier id;

	private TaskIdentifier parentId;

	private String name;

	private String description;

	private UserIdentifier owner;

	private Calendar created;

	private Calendar modified;

	private Calendar due;

	private Calendar start;

	private Long duration;

	private Integer priority = 0;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void setId(final TaskIdentifier id) {
		this.id = id;
	}

	@Override
	public TaskIdentifier getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

	public void setCompleted(final Boolean completed) {
		this.completed = completed;
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

	@Override
	public Long getDuration() {
		return duration;
	}

	@Override
	public Calendar getStart() {
		return start;
	}

	@Override
	public Calendar getDue() {
		return due;
	}

	public void setDue(final Calendar due) {
		this.due = due;
	}

	public void setStart(final Calendar start) {
		this.start = start;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}

	@Override
	public Boolean getCompleted() {
		return completed;
	}

	@Override
	public TaskIdentifier getParentId() {
		return parentId;
	}

	public void setParentId(final TaskIdentifier parentId) {
		this.parentId = parentId;
	}

	@Override
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(final Integer priority) {
		this.priority = priority;
	}

}
