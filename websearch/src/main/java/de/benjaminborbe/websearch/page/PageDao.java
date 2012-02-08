package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;

import de.benjaminborbe.tools.dao.Dao;

public interface PageDao extends Dao<PageBean, URL> {

	Collection<PageBean> findExpiredPages();

	PageBean findOrCreate(URL url);

	Collection<PageBean> findSubPages(URL url);

}
