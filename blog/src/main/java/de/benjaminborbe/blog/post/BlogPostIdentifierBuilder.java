package de.benjaminborbe.blog.post;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.blog.api.BlogPostIdentifier;

public class BlogPostIdentifierBuilder implements IdentifierBuilder<String, BlogPostIdentifier> {

	@Override
	public BlogPostIdentifier buildIdentifier(final String value) {
		return new BlogPostIdentifier(value);
	}

}
