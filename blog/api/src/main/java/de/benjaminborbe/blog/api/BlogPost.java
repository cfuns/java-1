package de.benjaminborbe.blog.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

import java.util.Calendar;

public interface BlogPost {

	BlogPostIdentifier getId();

	String getTitle();

	String getContent();

	UserIdentifier getCreator();

	Calendar getCreated();

	Calendar getModified();
}
