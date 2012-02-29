package de.benjaminborbe.bookmark.api;

import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface BookmarkService {

	boolean createBookmark(SessionIdentifier sessionIdentifier, final String url, final String name, final String description, final List<String> keywords, boolean favorite)
			throws BookmarkServiceException;

	boolean deleteBookmark(SessionIdentifier sessionIdentifier, final BookmarkIdentifier bookmarkIdentifier) throws BookmarkServiceException;

	List<Bookmark> getBookmarks(SessionIdentifier sessionIdentifier) throws BookmarkServiceException;

	List<Bookmark> searchBookmarks(SessionIdentifier sessionIdentifier, String[] words) throws BookmarkServiceException;

	List<Bookmark> getBookmarkFavorite(SessionIdentifier sessionIdentifier) throws BookmarkServiceException;

	BookmarkIdentifier createBookmarkIdentifier(SessionIdentifier sessionIdentifier, String url) throws BookmarkServiceException;
}
