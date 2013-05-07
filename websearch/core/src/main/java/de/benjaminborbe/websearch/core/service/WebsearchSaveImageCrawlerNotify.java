package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
import de.benjaminborbe.websearch.core.action.WebsearchSaveAllImage;
import de.benjaminborbe.websearch.core.config.WebsearchConfig;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Singleton
public class WebsearchSaveImageCrawlerNotify implements CrawlerNotifier {

	private final Logger logger;

	private final WebsearchSaveAllImage websearchSaveAllImage;

	private final WebsearchConfig websearchConfig;

	@Inject
	public WebsearchSaveImageCrawlerNotify(final Logger logger, final WebsearchSaveAllImage websearchSaveAllImage, final WebsearchConfig websearchConfig) {
		this.logger = logger;
		this.websearchSaveAllImage = websearchSaveAllImage;
		this.websearchConfig = websearchConfig;
	}

	@Override
	public void notifiy(final CrawlerNotifierResult httpResponse) {
		if (websearchConfig.saveImages()) {
			try {
				websearchSaveAllImage.saveImage(httpResponse);
			} catch (NoSuchAlgorithmException | IOException e) {
				logger.debug("saveImage failed", e);
			}
		}
	}

}
