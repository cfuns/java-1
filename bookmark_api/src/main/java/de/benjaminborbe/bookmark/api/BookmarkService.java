package de.benjaminborbe.bookmark.api;

import java.util.List;

public interface BookmarkService {

	List<Bookmark> getBookmarks();

	List<Bookmark> searchBookmarks(String[] words);

	List<Bookmark> getBookmarkFavoritie();

}
