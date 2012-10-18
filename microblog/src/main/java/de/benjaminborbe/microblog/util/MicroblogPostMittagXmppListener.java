package de.benjaminborbe.microblog.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

public class MicroblogPostMittagXmppListener implements MicroblogPostListener {

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
		logger.debug("onNewPost");
		try {
			final MicroblogPostResult microblogPostResult = microblogConnector.getPost(microblogPostIdentifier);
			final String content = microblogPostResult.getContent();
			if (content != null && (content.indexOf("Mittagessen") != -1 || content.indexOf("Mittagstisch") != -1)) {
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

}
