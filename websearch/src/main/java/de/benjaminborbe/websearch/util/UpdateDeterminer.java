package de.benjaminborbe.websearch.util;

import java.util.Collection;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.websearch.page.PageBean;

public interface UpdateDeterminer {

	Collection<PageBean> determineExpiredPages() throws StorageException, EntityIteratorException;
}
