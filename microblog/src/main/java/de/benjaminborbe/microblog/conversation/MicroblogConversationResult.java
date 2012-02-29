package de.benjaminborbe.microblog.conversation;

import java.util.List;

import de.benjaminborbe.microblog.post.MicroblogPostResult;

public class MicroblogConversationResult {

	private final List<MicroblogPostResult> posts;

	public MicroblogConversationResult(final List<MicroblogPostResult> posts) {
		this.posts = posts;
	}

	public List<MicroblogPostResult> getPosts() {
		return posts;
	}

}
