package de.benjaminborbe.websearch.util;

import java.util.Collection;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.websearch.page.WebsearchPageBean;

public interface WebsearchUpdateDeterminer {

	Collection<WebsearchPageBean> determineExpiredPages() throws StorageException, EntityIteratorException;
}
