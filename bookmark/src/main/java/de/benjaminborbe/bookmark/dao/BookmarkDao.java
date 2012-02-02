package de.benjaminborbe.bookmark.dao;

import java.util.Collection;

import de.benjaminborbe.tools.dao.Dao;

public interface BookmarkDao extends Dao<BookmarkBean, Long> {

	Collection<BookmarkBean> getFavorites();
}
