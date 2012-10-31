package de.benjaminborbe.task.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface Task {

	TaskIdentifier getId();

	String getName();

	String getDescription();

	UserIdentifier getOwner();

	boolean isCompleted();
}
