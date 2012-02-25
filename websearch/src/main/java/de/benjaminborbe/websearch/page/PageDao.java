package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.websearch.api.PageIdentifier;

public interface PageDao extends Dao<PageBean, PageIdentifier> {

	PageBean findOrCreate(URL url) throws StorageException;

	Collection<PageBean> findSubPages(URL url) throws StorageException;

	PageBean load(URL url) throws StorageException;

}
