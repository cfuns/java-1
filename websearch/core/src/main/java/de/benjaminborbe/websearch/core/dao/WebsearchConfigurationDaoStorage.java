package de.benjaminborbe.websearch.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;
import de.benjaminborbe.websearch.core.util.WebsearchConfigurationActivatedPredicate;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WebsearchConfigurationDaoStorage extends DaoStorage<WebsearchConfigurationBean, WebsearchConfigurationIdentifier> implements WebsearchConfigurationDao {

	private static final String COLUMNFAMILY = "websearch_configuration";

	@Inject
	public WebsearchConfigurationDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<WebsearchConfigurationBean> beanProvider,
		final WebsearchConfigurationBeanMapper pageBeanMapper,
		final CalendarUtil calendarUtil,
		final WebsearchConfigurationIdentifierBuilder identifierBuilder
	) {
		super(logger, storageService, beanProvider, pageBeanMapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMNFAMILY;
	}

	@Override
	public EntityIterator<WebsearchConfigurationBean> getActivatedEntityIterator() throws StorageException {
		return new EntityIteratorFilter<>(getEntityIterator(), new WebsearchConfigurationActivatedPredicate());
	}
}
