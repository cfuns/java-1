package de.benjaminborbe.index.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.dao.Bookmark;
import de.benjaminborbe.index.dao.BookmarkDao;

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

	private final Comparator<Bookmark> c = new BookmarkByNameComparator();

	private final Logger logger;

	private final BookmarkDao bookmarkDao;

	@Inject
	public BookmarkServiceImpl(final Logger logger, final BookmarkDao bookmarkDao) {
		this.logger = logger;
		this.bookmarkDao = bookmarkDao;
	}

	@Override
	public List<Bookmark> getBookmarks() {
		logger.debug("getBookmarks");
		final List<Bookmark> bookmarks = new ArrayList<Bookmark>(bookmarkDao.getAll());
		Collections.sort(bookmarks, c);
		return bookmarks;
	}

	@Override
	public List<Bookmark> searchBookmarks(final String search) {
		final String[] parts = search.split("\\s+");

		final List<Match> matches = new ArrayList<Match>();
		for (final Bookmark bookmark : getBookmarks()) {
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
		return bookmark.getUrl().toLowerCase().indexOf(search.toLowerCase()) != -1
				|| bookmark.getName().toLowerCase().indexOf(search.toLowerCase()) != -1;
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
