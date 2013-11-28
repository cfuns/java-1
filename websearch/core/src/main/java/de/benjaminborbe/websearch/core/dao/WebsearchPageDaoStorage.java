package de.benjaminborbe.websearch.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.core.util.WebsearchPageContentUpdateHandler;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

@Singleton
public class WebsearchPageDaoStorage extends DaoStorage<WebsearchPageBean, WebsearchPageIdentifier> implements WebsearchPageDao {

	public static final String COLUMNFAMILY = "websearch_page";

	private final WebsearchPageDaoSubPagesAction pageDaoSubPagesAction;

	private final WebsearchPageContentUpdateHandler websearchPageContentUpdateHandler;

	@Inject
	public WebsearchPageDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<WebsearchPageBean> beanProvider,
		final WebsearchPageDaoSubPagesAction pageDaoSubPagesAction,
		final WebsearchPageBeanMapper pageBeanMapper,
		final WebsearchPageIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil,
		final WebsearchPageContentUpdateHandler websearchPageContentUpdateHandler
	) {
		super(logger, storageService, beanProvider, pageBeanMapper, identifierBuilder, calendarUtil);
		this.pageDaoSubPagesAction = pageDaoSubPagesAction;
		this.websearchPageContentUpdateHandler = websearchPageContentUpdateHandler;
	}

	@Override
	public WebsearchPageBean load(final URL url) throws StorageException {
		return load(new WebsearchPageIdentifier(url.toExternalForm()));
	}

	@Override
	public WebsearchPageBean findOrCreate(final URL url) throws StorageException {
		{
			final WebsearchPageBean page = load(url);
			if (page != null) {
				return page;
			}
		}
		{
			final WebsearchPageBean page = create();
			page.setId(new WebsearchPageIdentifier(url.toExternalForm()));
			page.setUrl(url);
			save(page);
			return page;
		}
	}

	@Override
	protected String getColumnFamily() {
		return COLUMNFAMILY;
	}

	@Override
	public Collection<WebsearchPageBean> findSubPages(final WebsearchPageIdentifier websearchPageIdentifier) throws StorageException {
		try {
			return pageDaoSubPagesAction.findSubPages(websearchPageIdentifier.getId(), getEntityIterator());
		} catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public Collection<WebsearchPageBean> findSubPages(final URL url) throws StorageException {
		try {
			return pageDaoSubPagesAction.findSubPages(url.toExternalForm(), getEntityIterator());
		} catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public void onPostSave(
		final WebsearchPageBean entity,
		final Collection<StorageValue> fieldNames
	) throws ParseException, IOException, IndexerServiceException {
		if (fieldNames == null || fieldNames.contains(new StorageValue(WebsearchPageBeanMapper.CONTENT, getEncoding()))) {
			websearchPageContentUpdateHandler.onContentUpdated(entity);
		}
	}
}
