package de.benjaminborbe.checklist.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface ChecklistEntry {

	ChecklistEntryIdentifier getId();

	ChecklistListIdentifier getListId();

	String getName();

	UserIdentifier getOwner();

	Boolean getCompleted();
}
