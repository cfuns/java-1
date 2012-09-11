package de.benjaminborbe.blog.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface BlogPost {

	BlogPostIdentifier getId();

	String getTitle();

	String getContent();

	UserIdentifier getCreator();
}
