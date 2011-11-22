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
		final List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		for (final Bookmark bookmark : getBookmarks()) {
			if (bookmark.getUrl().toLowerCase().indexOf(search.toLowerCase()) != -1
					|| bookmark.getName().toLowerCase().indexOf(search.toLowerCase()) != -1) {
				bookmarks.add(bookmark);
			}
		}
		return bookmarks;
	}
}
