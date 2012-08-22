package de.benjaminborbe.bookmark.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;

public class BookmarkIdentifierBuilder implements IdentifierBuilder<String, BookmarkIdentifier> {

	@Override
	public BookmarkIdentifier buildIdentifier(final String value) {
		return new BookmarkIdentifier(value);
	}

}
