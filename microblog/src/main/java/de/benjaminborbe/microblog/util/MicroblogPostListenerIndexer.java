package de.benjaminborbe.microblog.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.microblog.MicroblogConstants;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class MicroblogPostListenerIndexer implements MicroblogPostListener {

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final IndexService indexService;

	private final ParseUtil parseUtil;

	@Inject
	public MicroblogPostListenerIndexer(final Logger logger, final MicroblogConnector microblogConnector, final IndexService indexService, final ParseUtil parseUtil) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.indexService = indexService;
		this.parseUtil = parseUtil;
	}

	@Override
	public void onNewPost(final MicroblogPostIdentifier microblogPostIdentifier) {
		try {
			logger.trace("onNewPost");
			final MicroblogPostResult microblogPostResult = microblogConnector.getPost(microblogPostIdentifier);
			final String content = microblogPostResult.getContent();
			final String url = microblogPostResult.getPostUrl();
			indexService.addToIndex(MicroblogConstants.INDEX, parseUtil.parseURL(url), content, content);
		}
		catch (final MicroblogConnectorException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final IndexerServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final ParseException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}

}
