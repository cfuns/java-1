package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.websearch.api.PageIdentifier;

public interface WebsearchPageDao extends Dao<WebsearchPageBean, PageIdentifier> {

	WebsearchPageBean findOrCreate(URL url) throws StorageException;

	Collection<WebsearchPageBean> findSubPages(URL url) throws StorageException;

	WebsearchPageBean load(URL url) throws StorageException;

}