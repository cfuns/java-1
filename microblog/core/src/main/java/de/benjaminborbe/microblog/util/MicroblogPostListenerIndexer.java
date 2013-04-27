package de.benjaminborbe.microblog.util;

import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.microblog.MicroblogConstants;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Calendar;

public class MicroblogPostListenerIndexer implements MicroblogPostListener {

	private final Logger logger;

	private final IndexService indexService;

	private final ParseUtil parseUtil;

	@Inject
	public MicroblogPostListenerIndexer(final Logger logger, final IndexService indexService, final ParseUtil parseUtil) {
		this.logger = logger;
		this.indexService = indexService;
		this.parseUtil = parseUtil;
	}

	@Override
	public void onNewPost(final MicroblogPost microblogPost) {
		try {
			logger.trace("onNewPost");
			final String content = microblogPost.getContent();
			final String url = microblogPost.getPostUrl();
			final Calendar date = microblogPost.getDate();
			indexService.addToIndex(MicroblogConstants.INDEX, parseUtil.parseURL(url), content, content, date);
		} catch (final IndexerServiceException | ParseException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}
}
