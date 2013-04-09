package de.benjaminborbe.blog.api;

import de.benjaminborbe.api.IdentifierBase;

public class BlogPostIdentifier extends IdentifierBase<String> {

	public BlogPostIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public BlogPostIdentifier(final String id) {
		super(id);
	}

}
