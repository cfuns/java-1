package de.benjaminborbe.blog.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface BlogPost {

	String getTitle();

	String getContent();

	UserIdentifier getCreator();
}
