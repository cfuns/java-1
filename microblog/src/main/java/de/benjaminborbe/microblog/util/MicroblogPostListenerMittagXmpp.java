package de.benjaminborbe.microblog.util;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

public class MicroblogPostListenerMittagXmpp implements MicroblogPostListener {

	private final Collection<String> words = Arrays.asList("Essen", "Mittagessen", "Mittagstisch");

	private final Logger logger;

	private final XmppService xmppService;

	private final MicroblogConfig microblogConfig;

	@Inject
	public MicroblogPostListenerMittagXmpp(final Logger logger, final MicroblogConfig microblogConfig, final XmppService xmppService) {
		this.logger = logger;
		this.microblogConfig = microblogConfig;
		this.xmppService = xmppService;
	}

	@Override
	public void onNewPost(final MicroblogPost microblogPost) {
		try {
			if (microblogConfig.isXmppEnabled()) {
				logger.trace("onNewPost");
				final String content = microblogPost.getContent();
				if (isLunch(content)) {
					logger.trace("isLunch = true, sending message");
					xmppService.send(content);
				}
			}
		}
		catch (final XmppServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	private boolean isLunch(final String content) {
		if (content != null) {
			for (final String word : words) {
				if (content.indexOf(word) != -1) {
					return true;
				}
			}
		}
		return false;
	}
}
