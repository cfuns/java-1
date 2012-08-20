package de.benjaminborbe.wiki.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;

@Singleton
public class WikiPageDaoImpl extends DaoStorage<WikiPageBean, WikiPageIdentifier> implements WikiPageDao {

	private static final String COLUMN_FAMILY = "wiki_page";

	@Inject
	public WikiPageDaoImpl(final Logger logger, final StorageService storageService, final Provider<WikiPageBean> beanProvider, final WikiPageBeanMapper mapper) {
		super(logger, storageService, beanProvider, mapper);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
