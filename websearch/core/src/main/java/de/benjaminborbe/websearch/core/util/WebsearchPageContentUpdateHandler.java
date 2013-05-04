package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.net.HttpURLConnection;

public class WebsearchPageContentUpdateHandler {

	private final Logger logger;

	private final HttpUtil httpUtil;

	private final WebsearchAddToSearchIndex websearchAddToSearchIndex;

	private final WebsearchParseLinks websearchParseLinks;

	@Inject
	public WebsearchPageContentUpdateHandler(
		final Logger logger,
		final HttpUtil httpUtil,
		final WebsearchAddToSearchIndex websearchAddToSearchIndex,
		final WebsearchParseLinks websearchParseLinks
	) {
		this.logger = logger;
		this.httpUtil = httpUtil;
		this.websearchAddToSearchIndex = websearchAddToSearchIndex;
		this.websearchParseLinks = websearchParseLinks;
	}

	public void onContentUpdated(final WebsearchPageBean page) throws ParseException, IndexerServiceException, IOException {
		if (isHtmlPage(page)) {
			websearchParseLinks.parseLinks(page);
			websearchAddToSearchIndex.addToIndex(page);
		}
	}

	protected boolean isHtmlPage(final HttpResponse result) {
		if (result.getReturnCode() == null || result.getReturnCode() != HttpURLConnection.HTTP_OK) {
			logger.trace("result not available for url: " + result.getUrl() + " returnCode: " + result.getReturnCode());
			return false;
		}
		if (!httpUtil.isHtml(result.getHeader())) {
			logger.trace("result has wrong contenttype for url: " + result.getUrl() + " contentType: " + httpUtil.getContentType(result.getHeader()));
			return false;
		}
		return true;
	}

}
