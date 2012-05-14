package de.benjaminborbe.bookmark.mock;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;

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

	@Override
	public boolean createBookmark(final SessionIdentifier sessionIdentifier, final String url, final String name, final String description, final List<String> keywords,
			final boolean favorite) throws BookmarkServiceException {
		return false;
	}

	@Override
	public BookmarkIdentifier createBookmarkIdentifier(final SessionIdentifier sessionIdentifier, final String url) throws BookmarkServiceException {
		return null;
	}

	@Override
	public boolean deleteBookmark(final SessionIdentifier sessionIdentifier, final BookmarkIdentifier bookmarkIdentifier) throws BookmarkServiceException {
		return false;
	}

	@Override
	public boolean updateBookmark(final SessionIdentifier sessionIdentifier, final BookmarkIdentifier bookmarkIdentifier, final String url, final String name,
			final String description, final List<String> keywords, final boolean favorite) throws BookmarkServiceException {
		return false;
	}

	@Override
	public Bookmark getBookmark(final SessionIdentifier sessionIdentifier, final BookmarkIdentifier bookmarkIdentifier) throws BookmarkServiceException {
		return null;
	}

}
