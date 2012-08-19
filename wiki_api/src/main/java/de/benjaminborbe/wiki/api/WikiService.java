package de.benjaminborbe.wiki.api;

import java.util.Collection;

public interface WikiService {

	Collection<WikiSpaceIdentifier> getSpaceIdentifiers();

	Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier);

	WikiPage getPage(WikiPageIdentifier wikiPageIdentifier);

	WikiSpaceIdentifier getSpaceByName(String spaceName);

	WikiPage getPageByName(WikiSpaceIdentifier wikiSpaceIdentifier, String pageName);

	WikiPageIdentifier createPage(WikiSpaceIdentifier wikiSpaceIdentifier, String pageTitle, String pageContent);

	void updatePage(WikiPageIdentifier wikiPageIdentifier, String pageTitle, String pageContent);

	void deletePage(WikiPageIdentifier wikiPageIdentifier);
}
