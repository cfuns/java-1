package de.benjaminborbe.search.core.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

@Singleton
public class SearchQueryHistoryDaoStorage extends DaoStorage<SearchQueryHistoryBean, SearchQueryHistoryIdentifier> implements SearchQueryHistoryDao {

	@Inject
	public SearchQueryHistoryDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<SearchQueryHistoryBean> beanProvider,
		final SearchQueryHistoryBeanMapper mapper,
		final SearchQueryHistoryIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "search_query_history";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
