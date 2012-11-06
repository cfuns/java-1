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
public class PageDaoCache extends DaoCache<PageBean, PageIdentifier> implements PageDao {

	private final PageDaoSubPagesAction pageDaoSubPagesAction;

	@Inject
	public PageDaoCache(final Logger logger, final Provider<PageBean> provider, final PageDaoSubPagesAction pageDaoSubPagesAction) {
		super(logger, provider);
		this.pageDaoSubPagesAction = pageDaoSubPagesAction;
	}

	@Override
	public PageBean findOrCreate(final URL url) {
		{
			final PageBean page = load(url);
			if (page != null) {
				return page;
			}
		}
		{
			final PageBean page = create();
			page.setUrl(url);
			save(page);
			return page;
		}
	}

	@Override
	public PageBean load(final URL url) {
		return load(new PageIdentifier(url));
	}

	@Override
	public Collection<PageBean> findSubPages(final URL url) throws StorageException {
		try {
			return pageDaoSubPagesAction.findSubPages(url, getEntityIterator());
		}
		catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

}
