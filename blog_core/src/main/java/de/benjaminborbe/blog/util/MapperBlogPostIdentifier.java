package de.benjaminborbe.blog.util;

import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperBlogPostIdentifier implements Mapper<BlogPostIdentifier> {

	@Override
	public String toString(final BlogPostIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public BlogPostIdentifier fromString(final String value) {
		return value != null ? new BlogPostIdentifier(value) : null;
	}

}
