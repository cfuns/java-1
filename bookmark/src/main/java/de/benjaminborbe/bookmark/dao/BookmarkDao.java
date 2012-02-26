package de.benjaminborbe.bookmark.dao;

import java.util.Collection;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;

public interface BookmarkDao extends Dao<BookmarkBean, BookmarkIdentifier> {

	Collection<BookmarkBean> getFavorites(UserIdentifier userIdentifier) throws StorageException;

	Collection<BookmarkBean> getByUsername(UserIdentifier userIdentifier) throws StorageException;
}
