package de.benjaminborbe.websearch.util;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.websearch.dao.WebsearchPageBean;

public interface WebsearchUpdateDeterminer {

	EntityIterator<WebsearchPageBean> determineExpiredPages() throws StorageException, EntityIteratorException;
}
