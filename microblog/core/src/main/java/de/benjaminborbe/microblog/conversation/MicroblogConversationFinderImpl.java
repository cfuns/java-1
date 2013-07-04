package de.benjaminborbe.microblog.conversation;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MicroblogConversationFinderImpl implements MicroblogConversationFinder {

	private final MicroblogConnector microblogConnector;

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public MicroblogConversationFinderImpl(final Logger logger, final MicroblogConnector microblogConnector, final ParseUtil parseUtil) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.parseUtil = parseUtil;
	}

	@Override
	public MicroblogConversationIdentifier findIdentifier(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogConnectorException, ParseException {
		logger.trace("findIdentifier");
		final MicroblogPostResult postResult = microblogConnector.getPost(microblogPostIdentifier);
		if (postResult == null) {
			logger.trace("postResult is null");
			return null;
		}
		final String conversationUrl = postResult.getConversationUrl();
		if (conversationUrl == null) {
			logger.trace("conversationUrl is null");
			return null;
		}
		final int lastSlashIndex = conversationUrl.lastIndexOf("/");
		if (lastSlashIndex == -1) {
			logger.trace("no slash in conversationUrl found");
			return null;
		}
		final int lastHashtagIndex = conversationUrl.lastIndexOf("#");
		if (lastHashtagIndex == -1) {
			logger.trace("no hash in conversationUrl found");
			return null;
		}
		final String idString = conversationUrl.substring(lastSlashIndex + 1, lastHashtagIndex);
		final long id = parseUtil.parseLong(idString);
		logger.trace("return conversationIdentifier with id " + id);
		return new MicroblogConversationIdentifier(id);
	}
}
