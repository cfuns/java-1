package de.benjaminborbe.checklist.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface ChecklistList {

	ChecklistListIdentifier getId();

	String getName();

	UserIdentifier getOwner();
}
