package de.benjaminborbe.bookmark.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;

public interface BookmarkDao extends Dao<BookmarkBean, BookmarkIdentifier> {

	EntityIterator<BookmarkBean> getFavorites(UserIdentifier userIdentifier) throws StorageException;

	EntityIterator<BookmarkBean> getByUsername(UserIdentifier userIdentifier) throws StorageException;
}
