package de.benjaminborbe.bookmark.mock;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;

@Singleton
public class BookmarkServiceMock implements BookmarkService {

	@Inject
	public BookmarkServiceMock() {
	}

	@Override
	public List<Bookmark> getBookmarks(final SessionIdentifier sessionIdentifier) {
		return null;
	}

	@Override
	public List<Bookmark> searchBookmarks(final SessionIdentifier sessionIdentifier, final String[] words) {
		return null;
	}

	@Override
	public List<Bookmark> getBookmarkFavorite(final SessionIdentifier sessionIdentifier) {
		return null;
	}

}
