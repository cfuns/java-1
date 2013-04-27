package de.benjaminborbe.wiki.dao;

import com.google.inject.Provider;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WikiPageDaoImpl extends DaoStorage<WikiPageBean, WikiPageIdentifier> implements WikiPageDao {

	private static final String COLUMN_FAMILY = "wiki_page";

	@Inject
	public WikiPageDaoImpl(
		final Logger logger,
		final StorageService storageService,
		final Provider<WikiPageBean> beanProvider,
		final WikiPageBeanMapper mapper,
		final WikiPageIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
