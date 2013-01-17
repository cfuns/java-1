package de.benjaminborbe.lunch.service;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostListener;

public class LunchMicroblogPostListener implements MicroblogPostListener {

	private final Logger logger;

	@Inject
	public LunchMicroblogPostListener(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void onNewPost(final MicroblogPost microblogPost) {
		logger.debug("onNewPost: " + microblogPost.getContent());

		// if match mittagessen
		// for each subscript user
		// notify
	}
}
