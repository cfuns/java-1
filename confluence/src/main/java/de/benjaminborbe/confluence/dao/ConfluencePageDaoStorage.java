package de.benjaminborbe.confluence.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluencePageIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.map.MapChain;

@Singleton
public class ConfluencePageDaoStorage extends DaoStorage<ConfluencePageBean, ConfluencePageIdentifier> implements ConfluencePageDao {

	@Inject
	public ConfluencePageDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<ConfluencePageBean> beanProvider,
			final ConfluencePageBeanMapper mapper,
			final ConfluencePageIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "confluence_page";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public ConfluencePageBean findOrCreate(final ConfluenceInstanceIdentifier instanceId, final String indexName, final String pageId) throws StorageException {
		final ConfluencePageIdentifier id = new ConfluencePageIdentifier(instanceId, indexName, pageId);
		{
			final ConfluencePageBean bean = load(id);
			if (bean != null) {
				return bean;
			}
		}
		{
			final ConfluencePageBean bean = create();
			bean.setId(id);
			return bean;
		}
	}

	@Override
	public EntityIterator<ConfluencePageBean> getPagesOfInstance(final ConfluenceInstanceIdentifier confluenceInstanceIdentifier) throws StorageException {
		return getEntityIterator(new MapChain<String, String>().add("instanceId", String.valueOf(confluenceInstanceIdentifier)));
	}

	@Override
	public long countPagesOfInstance(final ConfluenceInstanceIdentifier confluenceInstanceIdentifier) throws StorageException, IdentifierIteratorException {
		long counter = 0;
		final IdentifierIterator<ConfluencePageIdentifier> i = getIdentifierIterator(new MapChain<String, String>().add("instanceId", String.valueOf(confluenceInstanceIdentifier)));
		while (i.hasNext()) {
			i.next();
			counter++;
		}
		return counter;
	}

}
