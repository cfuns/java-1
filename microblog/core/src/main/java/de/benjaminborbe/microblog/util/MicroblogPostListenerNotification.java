package de.benjaminborbe.microblog.util;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostListener;

public class MicroblogPostListenerNotification implements MicroblogPostListener {

	private final Logger logger;

	private final MicroblogNotification microblogNotification;

	@Inject
	public MicroblogPostListenerNotification(final Logger logger, final MicroblogNotification microblogNotification) {
		this.logger = logger;
		this.microblogNotification = microblogNotification;
	}

	@Override
	public void onNewPost(final MicroblogPost microblogPost) {
		logger.debug("onNewPost");

		final String content = microblogPost.getContent();

		final Collection<UserIdentifier> users = microblogNotification.getUsersWithNotifications();
		for (final UserIdentifier user : users) {
			final Collection<String> keywords = microblogNotification.listNotifications(user);
			if (match(content, keywords)) {
			}
		}
	}

	private boolean match(final String content, final Collection<String> keywords) {
		final String lowerContent = content.toLowerCase();
		for (final String keyword : keywords) {
			if (keyword != null && !keyword.trim().isEmpty() && lowerContent.contains(keyword.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
