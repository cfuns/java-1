package de.benjaminborbe.bookmark.api;

import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface BookmarkService {

	List<Bookmark> getBookmarks(SessionIdentifier sessionIdentifier);

	List<Bookmark> searchBookmarks(SessionIdentifier sessionIdentifier, String[] words);

	List<Bookmark> getBookmarkFavorite(SessionIdentifier sessionIdentifier);

}
