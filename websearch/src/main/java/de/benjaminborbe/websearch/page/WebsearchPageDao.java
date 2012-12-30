package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

public interface WebsearchPageDao extends Dao<WebsearchPageBean, WebsearchPageIdentifier> {

	WebsearchPageBean findOrCreate(URL url) throws StorageException;

	WebsearchPageBean load(URL url) throws StorageException;

	Collection<WebsearchPageBean> findSubPages(URL url) throws StorageException;

	Collection<WebsearchPageBean> findSubPages(WebsearchPageIdentifier websearchPageIdentifier) throws StorageException;

}
