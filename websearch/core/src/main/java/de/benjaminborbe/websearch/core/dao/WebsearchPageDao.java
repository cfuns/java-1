package de.benjaminborbe.websearch.core.dao;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

import java.net.URL;
import java.util.Collection;

public interface WebsearchPageDao extends Dao<WebsearchPageBean, WebsearchPageIdentifier> {

	WebsearchPageBean load(URL url) throws StorageException;

	Collection<WebsearchPageBean> findSubPages(URL url) throws StorageException;

	Collection<WebsearchPageBean> findSubPages(WebsearchPageIdentifier websearchPageIdentifier) throws StorageException;

	WebsearchPageBean findOrCreate(URL url, Long depth, Integer timeout) throws StorageException;

}
