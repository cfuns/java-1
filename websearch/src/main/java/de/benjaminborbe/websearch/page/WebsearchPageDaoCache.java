package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.DaoCache;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.websearch.api.PageIdentifier;

@Singleton
public class WebsearchPageDaoCache extends DaoCache<WebsearchPageBean, PageIdentifier> implements WebsearchPageDao {

	private final WebsearchPageDaoSubPagesAction pageDaoSubPagesAction;

	@Inject
	public WebsearchPageDaoCache(final Logger logger, final Provider<WebsearchPageBean> provider, final WebsearchPageDaoSubPagesAction pageDaoSubPagesAction) {
		super(logger, provider);
		this.pageDaoSubPagesAction = pageDaoSubPagesAction;
	}

	@Override
	public WebsearchPageBean findOrCreate(final URL url) {
		{
			final WebsearchPageBean page = load(url);
			if (page != null) {
				return page;
			}
		}
		{
			final WebsearchPageBean page = create();
			page.setUrl(url);
			save(page);
			return page;
		}
	}

	@Override
	public WebsearchPageBean load(final URL url) {
		return load(new PageIdentifier(url));
	}

	@Override
	public Collection<WebsearchPageBean> findSubPages(final URL url) throws StorageException {
		try {
			return pageDaoSubPagesAction.findSubPages(url, getEntityIterator());
		}
		catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

}
