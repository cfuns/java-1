package de.benjaminborbe.microblog.post;

import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Calendar;

public class MicroblogPostResult implements MicroblogPost {

	private final String content;

	private final String author;

	private final String postUrl;

	private final String conversationUrl;

	private final MicroblogPostIdentifier id;

	private final Calendar date;

	public MicroblogPostResult(final MicroblogPostIdentifier id, final String content, final String author, final String postUrl, final String conversationUrl, final Calendar date) {
		this.id = id;
		this.content = content;
		this.author = author;
		this.postUrl = postUrl;
		this.conversationUrl = conversationUrl;
		this.date = date;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public String getPostUrl() {
		return postUrl;
	}

	@Override
	public String getConversationUrl() {
		return conversationUrl;
	}

	@Override
	public MicroblogPostIdentifier getId() {
		return id;
	}

	@Override
	public Calendar getDate() {
		return date;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.toString();
	}
}
