package de.benjaminborbe.wiki.api;

import java.util.Collection;

public interface WikiService {

	WikiSpaceIdentifier createSpace(String spaceIdentifier, String spaceTitle) throws WikiServiceException, WikiSpaceCreateException;

	boolean deleteSpace(WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException;

	WikiPageIdentifier createPage(WikiSpaceIdentifier wikiSpaceIdentifier, String pageTitle, String pageContent) throws WikiServiceException, WikiPageCreateException;

	boolean updatePage(WikiPageIdentifier wikiPageIdentifier, String pageTitle, String pageContent) throws WikiServiceException;

	boolean deletePage(WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException;

	Collection<WikiSpaceIdentifier> getSpaceIdentifiers() throws WikiServiceException;

	Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException;

	WikiPage getPage(WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException, WikiPageNotFoundException;

	WikiSpaceIdentifier getSpaceByName(String spaceName) throws WikiServiceException;

	WikiPage getPageByName(WikiSpaceIdentifier wikiSpaceIdentifier, String pageName) throws WikiServiceException, WikiPageNotFoundException;

	String renderPage(WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException, WikiPageNotFoundException;

	WikiPageIdentifier getPageIdentifierByName(WikiSpaceIdentifier wikiSpaceIdentifier, String pageName);

	WikiPageIdentifier createPageIdentifier(String id) throws WikiServiceException;

	WikiSpaceIdentifier createSpaceIdentifier(String id) throws WikiServiceException;

}
