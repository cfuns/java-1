package de.benjaminborbe.wiki.api;

import java.util.Collection;

public interface WikiService {

	WikiSpaceIdentifier createSpace(String spaceName) throws WikiServiceException;

	boolean deleteSpace(WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException;

	Collection<WikiSpaceIdentifier> getSpaceIdentifiers() throws WikiServiceException;

	Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException;

	WikiPage getPage(WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException;

	WikiSpaceIdentifier getSpaceByName(String spaceName) throws WikiServiceException;

	WikiPage getPageByName(WikiSpaceIdentifier wikiSpaceIdentifier, String pageName) throws WikiServiceException;

	WikiPageIdentifier createPage(WikiSpaceIdentifier wikiSpaceIdentifier, String pageTitle, String pageContent) throws WikiServiceException;

	boolean updatePage(WikiPageIdentifier wikiPageIdentifier, String pageTitle, String pageContent) throws WikiServiceException;

	boolean deletePage(WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException;

}
