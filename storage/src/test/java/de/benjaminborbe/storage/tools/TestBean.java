package de.benjaminborbe.storage.tools;

public class TestBean implements Entity<TestIdentifier> {

	private static final long serialVersionUID = -4849861781774372129L;

	private TestIdentifier id;

	private String name;

	@Override
	public TestIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final TestIdentifier id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
