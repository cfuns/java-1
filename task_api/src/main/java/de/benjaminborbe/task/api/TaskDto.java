package de.benjaminborbe.task.api;

import java.util.Calendar;
import java.util.Collection;

import de.benjaminborbe.authentication.api.UserIdentifier;

public class TaskDto implements Task {

	private Boolean completed;

	private TaskIdentifier id;

	private TaskIdentifier parentId;

	private String name;

	private String description;

	private UserIdentifier owner;

	private Calendar due;

	private Calendar start;

	private Long duration;

	private Integer priority;

	private Calendar completionDate;

	private Long repeatStart;

	private Long repeatDue;

	private String url;

	private Collection<TaskContextIdentifier> contexts;

	private TaskFocus focus;

	public TaskDto() {
	}

	public TaskDto(final Task task, final Collection<TaskContextIdentifier> contexts) {
		this.contexts = contexts;
		setCompleted(task.getCompleted());
		setId(task.getId());
		setParentId(task.getParentId());
		setName(task.getName());
		setDescription(task.getDescription());
		setOwner(task.getOwner());
		setDue(task.getDue());
		setStart(task.getStart());
		setDuration(task.getDuration());
		setPriority(task.getPriority());
		setCompletionDate(task.getCompletionDate());
		setRepeatStart(task.getRepeatStart());
		setRepeatDue(task.getRepeatDue());
		setUrl(task.getUrl());
		setFocus(task.getFocus());
	}

	@Override
	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(final Boolean completed) {
		this.completed = completed;
	}

	@Override
	public TaskIdentifier getId() {
		return id;
	}

	public void setId(final TaskIdentifier id) {
		this.id = id;
	}

	@Override
	public TaskIdentifier getParentId() {
		return parentId;
	}

	public void setParentId(final TaskIdentifier parentId) {
		this.parentId = parentId;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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

	@Override
	public Calendar getDue() {
		return due;
	}

	public void setDue(final Calendar due) {
		this.due = due;
	}

	@Override
	public Calendar getStart() {
		return start;
	}

	public void setStart(final Calendar start) {
		this.start = start;
	}

	@Override
	public Long getDuration() {
		return duration;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}

	@Override
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(final Integer priority) {
		this.priority = priority;
	}

	@Override
	public Calendar getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(final Calendar completionDate) {
		this.completionDate = completionDate;
	}

	@Override
	public Long getRepeatStart() {
		return repeatStart;
	}

	public void setRepeatStart(final Long repeatStart) {
		this.repeatStart = repeatStart;
	}

	@Override
	public Long getRepeatDue() {
		return repeatDue;
	}

	public void setRepeatDue(final Long repeatDue) {
		this.repeatDue = repeatDue;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public Collection<TaskContextIdentifier> getContexts() {
		return contexts;
	}

	public void setContexts(final Collection<TaskContextIdentifier> contexts) {
		this.contexts = contexts;
	}

	@Override
	public TaskFocus getFocus() {
		return focus;
	}

	public void setFocus(final TaskFocus focus) {
		this.focus = focus;
	}
}
