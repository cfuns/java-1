package de.benjaminborbe.confluence.dao;

import com.google.inject.Provider;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.util.ConfluenceInstanceActivatedPredicate;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConfluenceInstanceDaoStorage extends DaoStorage<ConfluenceInstanceBean, ConfluenceInstanceIdentifier> implements ConfluenceInstanceDao {

	@Inject
	public ConfluenceInstanceDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<ConfluenceInstanceBean> beanProvider,
		final ConfluenceInstanceBeanMapper mapper,
		final ConfluenceInstanceIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "confluence_instance";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public EntityIterator<ConfluenceInstanceBean> getActivatedEntityIterator() throws StorageException {
		return new EntityIteratorFilter<ConfluenceInstanceBean>(getEntityIterator(), new ConfluenceInstanceActivatedPredicate());
	}

}
