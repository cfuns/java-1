package de.benjaminborbe.checklist.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public class ChecklistEntryDto implements ChecklistEntry {

	private String name;

	private ChecklistEntryIdentifier id;

	private ChecklistListIdentifier listId;

	private UserIdentifier owner;

	@Override
	public ChecklistEntryIdentifier getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setId(final ChecklistEntryIdentifier id) {
		this.id = id;
	}

	@Override
	public ChecklistListIdentifier getListId() {
		return listId;
	}

	public void setListId(final ChecklistListIdentifier listId) {
		this.listId = listId;
	}

	@Override
	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

}
