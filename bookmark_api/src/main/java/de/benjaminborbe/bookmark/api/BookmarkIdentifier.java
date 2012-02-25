package de.benjaminborbe.bookmark.api;

import de.benjaminborbe.api.IdentifierBase;

public class BookmarkIdentifier extends IdentifierBase<String> {

	public BookmarkIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public BookmarkIdentifier(final String id) {
		super(id);
	}

}
