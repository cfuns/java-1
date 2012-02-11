package de.benjaminborbe.bookmark.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.bookmark.dao.BookmarkDao;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public class BookmarkServiceImpl implements BookmarkService {

	private final class MatchComparator implements Comparator<Match> {

		@Override
		public int compare(final Match a, final Match b) {
			if (a.getCounter() > b.getCounter()) {
				return -1;
			}
			if (a.getCounter() < b.getCounter()) {
				return 1;
			}
			return 0;
		}
	}

	private final class BookmarkByNameComparator implements Comparator<Bookmark> {

		@Override
		public int compare(final Bookmark a, final Bookmark b) {
			return a.getName().compareTo(b.getName());
		}
	}

	private final Comparator<Bookmark> bookmarkComparator = new BookmarkByNameComparator();

	private final Logger logger;

	private final BookmarkDao bookmarkDao;

	private final AuthenticationService authenticationService;

	@Inject
	public BookmarkServiceImpl(final Logger logger, final AuthenticationService authenticationService, final BookmarkDao bookmarkDao) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.bookmarkDao = bookmarkDao;
	}

	@Override
	public List<Bookmark> getBookmarks(final SessionIdentifier sessionIdentifier) throws BookmarkServiceException {
		try {
			logger.trace("getBookmarks");
			final List<Bookmark> bookmarks = new ArrayList<Bookmark>(bookmarkDao.getAll());
			Collections.sort(bookmarks, bookmarkComparator);
			return bookmarks;
		}
		catch (final StorageException e) {
			throw new BookmarkServiceException("StorageException", e);
		}
	}

	@Override
	public List<Bookmark> getBookmarkFavorite(final SessionIdentifier sessionIdentifier) throws BookmarkServiceException {
		try {
			logger.trace("getBookmarkFavorite");
			UserIdentifier userIdentifier;
			userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final List<Bookmark> bookmarks = new ArrayList<Bookmark>(bookmarkDao.getFavorites(userIdentifier));
			Collections.sort(bookmarks, bookmarkComparator);
			return bookmarks;
		}
		catch (final AuthenticationServiceException e) {
			throw new BookmarkServiceException("AuthenticationServiceException", e);
		}
	}

	@Override
	public List<Bookmark> searchBookmarks(final SessionIdentifier sessionIdentifier, final String[] parts) throws BookmarkServiceException {
		final List<Match> matches = new ArrayList<Match>();
		for (final Bookmark bookmark : getBookmarks(sessionIdentifier)) {
			final int counter = match(bookmark, parts);
			if (counter > 0) {
				final Match match = new Match(bookmark, counter);
				matches.add(match);
			}
		}
		Collections.sort(matches, new MatchComparator());
		final List<Bookmark> result = new ArrayList<Bookmark>();
		for (final Match match : matches) {
			result.add(match.getBookmark());
		}
		return result;
	}

	protected int match(final Bookmark bookmark, final String... searchTerms) {
		int counter = 0;
		for (final String searchTerm : searchTerms) {
			if (searchTerm != null && searchTerm.length() > 0 && match(bookmark, searchTerm)) {
				counter++;
			}
		}
		return counter;
	}

	protected boolean match(final Bookmark bookmark, final String search) {
		for (final String value : getSearchValues(bookmark)) {
			if (value.toLowerCase().indexOf(search.toLowerCase()) != -1) {
				return true;
			}
		}
		return false;
	}

	protected Collection<String> getSearchValues(final Bookmark bookmark) {
		final Set<String> values = new HashSet<String>();
		values.add(bookmark.getUrl());
		values.add(bookmark.getName());
		values.add(bookmark.getDescription());
		values.addAll(bookmark.getKeywords());
		return values;
	}

	private final class Match {

		private final Bookmark bookmark;

		private final int counter;

		public Match(final Bookmark bookmark, final int counter) {
			this.bookmark = bookmark;
			this.counter = counter;
		}

		public Bookmark getBookmark() {
			return bookmark;
		}

		public int getCounter() {
			return counter;
		}

	}
}
