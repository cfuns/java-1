package de.benjaminborbe.blog.gui.servlet;

import de.benjaminborbe.blog.gui.atom.Author;

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
