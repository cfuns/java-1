package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;

public interface WebsearchUpdateDeterminer {

	EntityIterator<WebsearchPageBean> determineExpiredPages() throws StorageException, EntityIteratorException;
}
