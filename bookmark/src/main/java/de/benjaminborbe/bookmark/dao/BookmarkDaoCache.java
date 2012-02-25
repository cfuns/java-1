package de.benjaminborbe.bookmark.dao;

import java.util.Collection;
import org.slf4j.Logger;

import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.storage.tools.DaoCacheAutoIncrement;

@Singleton
public class BookmarkDaoCache extends DaoCacheAutoIncrement<BookmarkBean, BookmarkIdentifier> implements BookmarkDao {

	@Inject
	public BookmarkDaoCache(final Logger logger, final BookmarkIdGenerator idGenerator, final Provider<BookmarkBean> provider) {
		super(logger, idGenerator, provider);
	}

	@Override
	public Collection<BookmarkBean> getFavorites(final UserIdentifier userIdentifier) {
		final Collection<BookmarkBean> bookmarks = getByUsername(userIdentifier);
		return Collections2.filter(bookmarks, new BookmarkFavoritePredicate());
	}

	@Override
	public Collection<BookmarkBean> getByUsername(final UserIdentifier userIdentifier) {
		final Collection<BookmarkBean> bookmarks = getAll();
		return Collections2.filter(bookmarks, new BookmarkOwnerPredicate(userIdentifier));
	}

}
