package de.benjaminborbe.task.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

import java.util.Calendar;

public interface Task {

	TaskIdentifier getId();

	TaskIdentifier getParentId();

	String getName();

	String getDescription();

	UserIdentifier getOwner();

	Boolean getCompleted();

	Long getDuration();

	Calendar getStart();

	Calendar getDue();

	Integer getPriority();

	Calendar getCompletionDate();

	Long getRepeatDue();

	Long getRepeatStart();

	String getUrl();

	TaskFocus getFocus();

	TaskContextIdentifier getContext();
}
