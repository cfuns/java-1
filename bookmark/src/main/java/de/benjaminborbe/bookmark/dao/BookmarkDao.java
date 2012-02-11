package de.benjaminborbe.bookmark.dao;

import java.util.Collection;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Dao;

public interface BookmarkDao extends Dao<BookmarkBean, Long> {

	Collection<BookmarkBean> getFavorites(UserIdentifier userIdentifier);

	Collection<BookmarkBean> getByUsername(UserIdentifier userIdentifier);
}
