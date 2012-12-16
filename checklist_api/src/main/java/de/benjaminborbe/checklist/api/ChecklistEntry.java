package de.benjaminborbe.checklist.api;

public interface ChecklistEntry {

	ChecklistEntryIdentifier getId();

	ChecklistListIdentifier getListId();

	String getName();
}
