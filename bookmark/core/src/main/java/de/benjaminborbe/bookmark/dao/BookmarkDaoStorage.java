package de.benjaminborbe.bookmark.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class BookmarkDaoStorage extends DaoStorage<BookmarkBean, BookmarkIdentifier> implements BookmarkDao {

	private static final String COLUMN_FAMILY = "bookmark";

	@Inject
	public BookmarkDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<BookmarkBean> beanProvider,
			final BookmarkBeanMapper mapper,
			final BookmarkIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public EntityIterator<BookmarkBean> getFavorites(final UserIdentifier userIdentifier) throws StorageException {
		return new EntityIteratorFilter<BookmarkBean>(getByUsername(userIdentifier), new BookmarkFavoritePredicate());
	}

	@Override
	public EntityIterator<BookmarkBean> getByUsername(final UserIdentifier userIdentifier) throws StorageException {
		return new EntityIteratorFilter<BookmarkBean>(getEntityIterator(), new BookmarkOwnerPredicate(userIdentifier));
	}

}
