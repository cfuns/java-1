package de.benjaminborbe.wiki.api;

import java.util.Collection;

public interface WikiService {

	WikiSpaceIdentifier createSpace(String spaceName);

	boolean deleteSpace(WikiSpaceIdentifier wikiSpaceIdentifier);

	WikiSpaceIdentifier createWikiSpaceIdentifier(String spaceName);

	WikiPageIdentifier createWikiPageIdentifier(String pageName);

	Collection<WikiSpaceIdentifier> getSpaceIdentifiers();

	Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier);

	WikiPage getPage(WikiPageIdentifier wikiPageIdentifier);

	WikiSpaceIdentifier getSpaceByName(String spaceName);

	WikiPage getPageByName(WikiSpaceIdentifier wikiSpaceIdentifier, String pageName);

	WikiPageIdentifier createPage(WikiSpaceIdentifier wikiSpaceIdentifier, String pageTitle, String pageContent);

	boolean updatePage(WikiPageIdentifier wikiPageIdentifier, String pageTitle, String pageContent);

	boolean deletePage(WikiPageIdentifier wikiPageIdentifier);
}
