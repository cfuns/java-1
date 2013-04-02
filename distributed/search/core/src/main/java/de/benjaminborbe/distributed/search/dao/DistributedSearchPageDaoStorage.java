package de.benjaminborbe.distributed.search.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

@Singleton
public class DistributedSearchPageDaoStorage extends DaoStorage<DistributedSearchPageBean, DistributedSearchPageIdentifier> implements DistributedSearchPageDao {

	@Inject
	public DistributedSearchPageDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<DistributedSearchPageBean> beanProvider,
		final DistributedSearchPageBeanMapper mapper,
		final DistributedSearchPageIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "distributed_search_page";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public IdentifierIterator<DistributedSearchPageIdentifier> getIdentifierIteratorByIndex(final String index) throws StorageException {
		return getIdentifierIterator(new StorageValueMap(getEncoding()).add(DistributedSearchPageBeanMapper.INDEX, index));
	}

	@Override
	public EntityIterator<DistributedSearchPageBean> getEntityIteratorByIndex(final String index) throws StorageException {
		return getEntityIterator(new StorageValueMap(getEncoding()).add(DistributedSearchPageBeanMapper.INDEX, index));
	}

}
