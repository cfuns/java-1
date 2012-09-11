package de.benjaminborbe.blog.api;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface BlogPost {

	BlogPostIdentifier getId();

	String getTitle();

	String getContent();

	UserIdentifier getCreator();

	Calendar getCreated();

	Calendar getModified();
}
