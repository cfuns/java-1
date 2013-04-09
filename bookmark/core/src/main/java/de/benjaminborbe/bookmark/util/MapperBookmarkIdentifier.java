package de.benjaminborbe.bookmark.util;

import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperBookmarkIdentifier implements Mapper<BookmarkIdentifier> {

	@Override
	public String toString(final BookmarkIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public BookmarkIdentifier fromString(final String value) {
		return value != null ? new BookmarkIdentifier(value) : null;
	}

}
