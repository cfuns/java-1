package de.benjaminborbe.storage.tools;

public class TestBean extends EntityBase<TestIdentifier> {

	private static final long serialVersionUID = -4849861781774372129L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
