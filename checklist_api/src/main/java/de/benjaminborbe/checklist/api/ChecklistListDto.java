package de.benjaminborbe.checklist.api;

public class ChecklistListDto implements ChecklistList {

	private String name;

	private ChecklistListIdentifier id;

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

}
