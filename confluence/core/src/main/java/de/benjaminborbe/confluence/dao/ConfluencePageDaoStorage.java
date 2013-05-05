package de.benjaminborbe.confluence.dao;

import com.google.inject.Provider;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluencePageIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConfluencePageDaoStorage extends DaoStorage<ConfluencePageBean, ConfluencePageIdentifier> implements ConfluencePageDao {

	private final Logger logger;

	@Inject
	public ConfluencePageDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<ConfluencePageBean> beanProvider,
		final ConfluencePageBeanMapper mapper,
		final ConfluencePageIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.logger = logger;
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
		return getEntityIterator(new StorageValueMap(getEncoding()).add("instanceId", String.valueOf(confluenceInstanceIdentifier)));
	}

	@Override
	public long countPagesOfInstance(final ConfluenceInstanceIdentifier confluenceInstanceIdentifier) throws StorageException {
		return count(new StorageValue("instanceId", getEncoding()), new StorageValue(String.valueOf(confluenceInstanceIdentifier), getEncoding()));
	}

	@Override
	public ConfluencePageIdentifier findPageByUrl(
		final ConfluenceInstanceIdentifier confluenceInstanceIdentifier, final String pageUrl
	) throws StorageException, IdentifierIteratorException {
		logger.trace("search for page with instance " + confluenceInstanceIdentifier + " and url: " + pageUrl);
		final StorageValueMap storageValueMap = new StorageValueMap(getEncoding());
		storageValueMap.add(ConfluencePageBeanMapper.INSTANCE_ID, confluenceInstanceIdentifier.getId());
		storageValueMap.add(ConfluencePageBeanMapper.URL, pageUrl);
		final IdentifierIterator<ConfluencePageIdentifier> identifierIterator = getIdentifierIterator(storageValueMap);
		if (identifierIterator.hasNext()) {
			ConfluencePageIdentifier confluencePageIdentifier = identifierIterator.next();
			logger.trace("found page: " + confluenceInstanceIdentifier);
			return confluencePageIdentifier;
		} else {
			logger.trace("didn't found page");
			return null;
		}
	}
}
