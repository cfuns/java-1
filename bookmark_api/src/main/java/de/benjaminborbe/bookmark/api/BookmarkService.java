package de.benjaminborbe.bookmark.api;

import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface BookmarkService {

	List<Bookmark> getBookmarks(SessionIdentifier sessionIdentifier) throws BookmarkServiceException;

	List<Bookmark> searchBookmarks(SessionIdentifier sessionIdentifier, String[] words) throws BookmarkServiceException;

	List<Bookmark> getBookmarkFavorite(SessionIdentifier sessionIdentifier) throws BookmarkServiceException;

}
