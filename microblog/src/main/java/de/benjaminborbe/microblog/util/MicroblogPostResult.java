package de.benjaminborbe.microblog.util;

public class MicroblogPostResult {

	private final String content;

	private final String author;

	private final String postUrl;

	private final String conversationUrl;

	public MicroblogPostResult(final String content, final String author, final String postUrl, final String conversationUrl) {
		this.content = content;
		this.author = author;
		this.postUrl = postUrl;
		this.conversationUrl = conversationUrl;
	}

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
}
