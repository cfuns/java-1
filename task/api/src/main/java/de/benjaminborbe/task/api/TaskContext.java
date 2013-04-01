package de.benjaminborbe.task.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface TaskContext {

	TaskContextIdentifier getId();

	String getName();

	UserIdentifier getOwner();

}
