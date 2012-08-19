package de.benjaminborbe.wiki.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

@Singleton
public class WikiServiceImpl implements WikiService {

	private final Logger logger;

	@Inject
	public WikiServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Collection<WikiSpaceIdentifier> getSpaceIdentifiers() {
		logger.trace("getSpaceIdentifiers");
		return null;
	}

	@Override
	public Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier) {
		logger.trace("getPageIdentifiers");
		return null;
	}

	@Override
	public WikiPage getPage(final WikiPageIdentifier wikiPageIdentifier) {
		logger.trace("getPage");
		return null;
	}

	@Override
	public WikiSpaceIdentifier getSpaceByName(final String spaceName) {
		logger.trace("getSpaceByName");
		return null;
	}

	@Override
	public WikiPage getPageByName(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageName) {
		logger.trace("getPageByName");
		return null;
	}

	@Override
	public WikiPageIdentifier createPage(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageTitle, final String pageContent) {
		logger.trace("createPage");
		return null;
	}

	@Override
	public void updatePage(final WikiPageIdentifier wikiPageIdentifier, final String pageTitle, final String pageContent) {
		logger.trace("updatePage");
	}

	@Override
	public void deletePage(final WikiPageIdentifier wikiPageIdentifier) {
		logger.trace("deletePage");
	}

}
