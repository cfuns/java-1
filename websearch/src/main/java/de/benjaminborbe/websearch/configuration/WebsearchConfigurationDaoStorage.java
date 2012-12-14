package de.benjaminborbe.websearch.configuration;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;
import de.benjaminborbe.websearch.util.WebsearchConfigurationActivatedPredicate;

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
			final WebsearchConfigurationIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, pageBeanMapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMNFAMILY;
	}

	@Override
	public EntityIterator<WebsearchConfigurationBean> getActivatedEntityIterator() throws StorageException {
		return new EntityIteratorFilter<WebsearchConfigurationBean>(getEntityIterator(), new WebsearchConfigurationActivatedPredicate());
	}
}
