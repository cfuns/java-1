package de.benjaminborbe.checklist.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public class ChecklistListDto implements ChecklistList {

	private String name;

	private ChecklistListIdentifier id;

	private UserIdentifier owner;

	@Override
	public ChecklistListIdentifier getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setId(final ChecklistListIdentifier id) {
		this.id = id;
	}

	@Override
	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

}
