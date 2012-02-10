package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageDao;
import de.benjaminborbe.storage.api.StorageService;

@Singleton
public class PageDaoStorage extends StorageDao<PageBean> implements PageDao {

	private final PageDaoSubPagesAction pageDaoSubPagesAction;

	@Inject
	public PageDaoStorage(final Logger logger, final StorageService storageService, final Provider<PageBean> beanProvider, final PageDaoSubPagesAction pageDaoSubPagesAction) {
		super(logger, storageService, beanProvider);
		this.pageDaoSubPagesAction = pageDaoSubPagesAction;
	}

	@Override
	public PageBean load(final URL url) {
		return load(url.toExternalForm());
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
	public Collection<PageBean> findSubPages(final URL url) {
		return pageDaoSubPagesAction.findSubPages(url, getAll());
	}

}
