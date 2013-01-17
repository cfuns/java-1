package de.benjaminborbe.microblog.post;

import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;

public class MicroblogPostResult implements MicroblogPost {

	private final String content;

	private final String author;

	private final String postUrl;

	private final String conversationUrl;

	private final MicroblogPostIdentifier id;

	public MicroblogPostResult(final MicroblogPostIdentifier id, final String content, final String author, final String postUrl, final String conversationUrl) {
		this.id = id;
		this.content = content;
		this.author = author;
		this.postUrl = postUrl;
		this.conversationUrl = conversationUrl;
	}

	@Override
	public String getContent() {
		return content;
	}

	public String getAuthor() {
		return author;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public String getConversationUrl() {
		return conversationUrl;
	}

	@Override
	public MicroblogPostIdentifier getId() {
		return id;
	}
}
