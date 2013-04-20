package de.benjaminborbe.wiki.mock;

import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

import java.util.Collection;

public class WikiServiceMock implements WikiService {

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
	public WikiSpaceIdentifier createSpace(final String spaceIdentifier, final String spaceTitle) throws WikiServiceException {
		return null;
	}

	@Override
	public String renderPage(final WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException {
		return null;
	}

	@Override
	public WikiPageIdentifier getPageIdentifierByName(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageName) {
		return null;
	}

	@Override
	public WikiPageIdentifier createPageIdentifier(final String id) throws WikiServiceException {
		return null;
	}

	@Override
	public WikiSpaceIdentifier createSpaceIdentifier(final String id) throws WikiServiceException {
		return null;
	}

	@Override
	public void deleteSpace(final WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException {
	}

	@Override
	public void updatePage(final WikiPageIdentifier wikiPageIdentifier, final String pageTitle, final String pageContent) throws WikiServiceException {
	}

	@Override
	public void deletePage() throws WikiServiceException {
	}

	@Override
	public Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException {
		return null;
	}

}
