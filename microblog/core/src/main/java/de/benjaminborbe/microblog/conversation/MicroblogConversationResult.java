package de.benjaminborbe.microblog.conversation;

import de.benjaminborbe.microblog.post.MicroblogPostResult;

import java.util.List;

public class MicroblogConversationResult {

	private final List<MicroblogPostResult> posts;

	private final String conversationUrl;

	public MicroblogConversationResult(final String conversationUrl, final List<MicroblogPostResult> posts) {
		this.conversationUrl = conversationUrl;
		this.posts = posts;
	}

	public List<MicroblogPostResult> getPosts() {
		return posts;
	}

	public String getConversationUrl() {
		return conversationUrl;
	}
}
