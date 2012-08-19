package de.benjaminborbe.wiki.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

@Singleton
public class WikiServiceMock implements WikiService {

	@Inject
	public WikiServiceMock() {
	}

	@Override
	public Collection<WikiSpaceIdentifier> getSpaceIdentifiers() {
		return null;
	}

	@Override
	public WikiPage getPage(final WikiPageIdentifier wikiPageIdentifier) {
		return null;
	}

	@Override
	public WikiSpaceIdentifier getSpaceByName(final String spaceName) {
		return null;
	}

	@Override
	public WikiPage getPageByName(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageName) {
		return null;
	}

	@Override
	public WikiPageIdentifier createPage(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageTitle, final String pageContent) {
		return null;
	}

	@Override
	public void updatePage(final WikiPageIdentifier wikiPageIdentifier, final String pageTitle, final String pageContent) {
	}

	@Override
	public void deletePage(final WikiPageIdentifier wikiPageIdentifier) {
	}

	@Override
	public Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier) {
		return null;
	}

}
