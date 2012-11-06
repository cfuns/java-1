package de.benjaminborbe.bookmark.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;

@Singleton
public class BookmarkDaoStorage extends DaoStorage<BookmarkBean, BookmarkIdentifier> implements BookmarkDao {

	private static final String COLUMN_FAMILY = "bookmark";

	@Inject
	public BookmarkDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<BookmarkBean> beanProvider,
			final BookmarkBeanMapper mapper,
			final BookmarkIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
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
		try {
			final Predicate<BookmarkBean> p = new BookmarkOwnerPredicate(userIdentifier);
			final List<BookmarkBean> result = new ArrayList<BookmarkBean>();
			final EntityIterator<BookmarkBean> i = getEntityIterator();
			while (i.hasNext()) {
				final BookmarkBean bookmark = i.next();
				if (p.apply(bookmark)) {
					result.add(bookmark);
				}
			}
			return result;
		}
		catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

}
