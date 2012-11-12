package de.benjaminborbe.task.api;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;

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

}
