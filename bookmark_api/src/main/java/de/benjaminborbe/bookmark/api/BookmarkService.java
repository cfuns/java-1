package de.benjaminborbe.bookmark.api;

import java.util.List;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface BookmarkService {

	String PERMISSION = "bookmark";

	void createBookmark(SessionIdentifier sessionIdentifier, final String url, final String name, final String description, final List<String> keywords, boolean favorite)
			throws BookmarkServiceException, LoginRequiredException, ValidationException;

	void updateBookmark(SessionIdentifier sessionIdentifier, BookmarkIdentifier bookmarkIdentifier, final String url, final String name, final String description,
			final List<String> keywords, boolean favorite) throws BookmarkServiceException, LoginRequiredException, ValidationException, PermissionDeniedException;

	void deleteBookmark(SessionIdentifier sessionIdentifier, final BookmarkIdentifier bookmarkIdentifier) throws BookmarkServiceException, BookmarkDeletionException,
			PermissionDeniedException, LoginRequiredException;

	List<Bookmark> getBookmarks(SessionIdentifier sessionIdentifier) throws BookmarkServiceException, LoginRequiredException, PermissionDeniedException;

	Bookmark getBookmark(SessionIdentifier sessionIdentifier, BookmarkIdentifier bookmarkIdentifier) throws BookmarkServiceException, PermissionDeniedException,
			LoginRequiredException;

	List<BookmarkMatch> searchBookmarks(SessionIdentifier sessionIdentifier, int limit, List<String> words) throws BookmarkServiceException, LoginRequiredException,
			PermissionDeniedException;

	List<Bookmark> getBookmarkFavorite(SessionIdentifier sessionIdentifier) throws BookmarkServiceException, LoginRequiredException, PermissionDeniedException;

	BookmarkIdentifier createBookmarkIdentifier(SessionIdentifier sessionIdentifier, String url) throws BookmarkServiceException;
}
