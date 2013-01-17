package de.benjaminborbe.microblog.api;

public interface MicroblogPost {

	MicroblogPostIdentifier getId();

	String getContent();

	String getPostUrl();

	String getConversationUrl();

	String getAuthor();
}
