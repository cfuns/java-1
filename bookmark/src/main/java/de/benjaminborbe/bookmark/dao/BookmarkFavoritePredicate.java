package de.benjaminborbe.bookmark.dao;

import com.google.common.base.Predicate;

public class BookmarkFavoritePredicate implements Predicate<BookmarkBean> {

	@Override
	public boolean apply(final BookmarkBean bookmark) {
		return bookmark.isFavorite();
	}
}
