package de.benjaminborbe.blog.gui.atom;

public class AuthorBean implements Author {

	private String name;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
