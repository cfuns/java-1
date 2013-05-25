package de.benjaminborbe.crawler.gui.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.gui.CrawlerGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

import javax.inject.Inject;

public class CrawlerGuiNavigationEntry implements NavigationEntry {

	private final CrawlerService crawlerService;

	@Inject
	public CrawlerGuiNavigationEntry(final CrawlerService crawlerService) {
		this.crawlerService = crawlerService;
	}

	@Override
	public String getTitle() {
		return "Crawler";
	}

	@Override
	public String getURL() {
		return "/" + CrawlerGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return crawlerService.hasPermission(sessionIdentifier);
		} catch (final CrawlerException e) {
			return false;
		}
	}

}
