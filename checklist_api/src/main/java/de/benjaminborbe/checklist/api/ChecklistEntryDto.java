package de.benjaminborbe.checklist.api;

public class ChecklistEntryDto implements ChecklistEntry {

	private String name;

	private ChecklistEntryIdentifier id;

	private ChecklistListIdentifier listId;

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

}
