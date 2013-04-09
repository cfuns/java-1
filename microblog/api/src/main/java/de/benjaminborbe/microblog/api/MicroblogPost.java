package de.benjaminborbe.microblog.api;

import java.util.Calendar;

public interface MicroblogPost {

	MicroblogPostIdentifier getId();

	String getContent();

	String getPostUrl();

	String getConversationUrl();

	String getAuthor();

	Calendar getDate();
}
