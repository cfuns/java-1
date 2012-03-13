package de.benjaminborbe.bookmark.dao;

import com.google.common.base.Predicate;

import de.benjaminborbe.authentication.api.UserIdentifier;

public class BookmarkOwnerPredicate implements Predicate<BookmarkBean> {

	private final UserIdentifier userIdentifier;

	BookmarkOwnerPredicate(final UserIdentifier userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	@Override
	public boolean apply(final BookmarkBean bookmark) {
		final Object username = userIdentifier.getId();
		return username.equals(bookmark.getOwnerUsername());
	}
}
