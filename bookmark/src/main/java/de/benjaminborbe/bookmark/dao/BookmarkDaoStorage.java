package de.benjaminborbe.bookmark.dao;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;

@Singleton
public class BookmarkDaoStorage extends DaoStorage<BookmarkBean, BookmarkIdentifier> implements BookmarkDao {

	private static final String COLUMN_FAMILY = "bookmark";

	@Inject
	public BookmarkDaoStorage(final Logger logger, final StorageService storageService, final Provider<BookmarkBean> beanProvider, final BookmarkBeanMapper mapper) {
		super(logger, storageService, beanProvider, mapper);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public Collection<BookmarkBean> getFavorites(final UserIdentifier userIdentifier) throws StorageException {
		final Collection<BookmarkBean> bookmarks = getByUsername(userIdentifier);
		return Collections2.filter(bookmarks, new BookmarkFavoritePredicate());
	}

	@Override
	public Collection<BookmarkBean> getByUsername(final UserIdentifier userIdentifier) throws StorageException {
		final Collection<BookmarkBean> bookmarks = getAll();
		return Collections2.filter(bookmarks, new BookmarkOwnerPredicate(userIdentifier));
	}

}
