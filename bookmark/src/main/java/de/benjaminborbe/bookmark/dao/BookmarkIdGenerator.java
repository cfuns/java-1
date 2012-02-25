package de.benjaminborbe.bookmark.dao;

import com.google.inject.Inject;

import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.tools.util.IdGenerator;
import de.benjaminborbe.tools.util.IdGeneratorLong;

public class BookmarkIdGenerator implements IdGenerator<BookmarkIdentifier> {

	private final IdGeneratorLong idGeneratorLong;

	@Inject
	public BookmarkIdGenerator(final IdGeneratorLong idGeneratorLong) {
		this.idGeneratorLong = idGeneratorLong;
	}

	@Override
	public BookmarkIdentifier nextId() {
		return new BookmarkIdentifier(String.valueOf(idGeneratorLong.nextId()));
	}

}
