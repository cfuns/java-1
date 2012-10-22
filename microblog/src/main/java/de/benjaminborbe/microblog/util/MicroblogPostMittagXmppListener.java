package de.benjaminborbe.microblog.util;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

public class MicroblogPostMittagXmppListener implements MicroblogPostListener {

	private final Collection<String> words = Arrays.asList("Essen", "Mittagessen", "Mittagstisch");

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final XmppService xmppService;

	@Inject
	public MicroblogPostMittagXmppListener(final Logger logger, final MicroblogConnector microblogConnector, final XmppService xmppService) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.xmppService = xmppService;
	}

	@Override
	public void onNewPost(final MicroblogPostIdentifier microblogPostIdentifier) {
		try {
			logger.trace("onNewPost");
			final MicroblogPostResult microblogPostResult = microblogConnector.getPost(microblogPostIdentifier);
			final String content = microblogPostResult.getContent();
			if (isLunch(content)) {
				logger.trace("isLunch = true, sending message");
				xmppService.send(content);
			}
		}
		catch (final MicroblogConnectorException e) {
			logger.debug(e.getClass().getName(), e);
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
