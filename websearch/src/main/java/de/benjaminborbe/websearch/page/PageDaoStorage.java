package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.StorageDao;

@Singleton
public class PageDaoStorage extends StorageDao<PageBean> implements PageDao {

	private static final String COLUMNFAMILY = "page";

	private final PageDaoSubPagesAction pageDaoSubPagesAction;

	@Inject
	public PageDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<PageBean> beanProvider,
			final PageDaoSubPagesAction pageDaoSubPagesAction,
			final PageBeanMapper pageBeanMapper) {
		super(logger, storageService, beanProvider, pageBeanMapper);
		this.pageDaoSubPagesAction = pageDaoSubPagesAction;
	}

	@Override
	public PageBean load(final URL url) throws StorageException {
		return load(url.toExternalForm());
	}

	@Override
	public PageBean findOrCreate(final URL url) throws StorageException {
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
	public Collection<PageBean> findSubPages(final URL url) throws StorageException {
		return pageDaoSubPagesAction.findSubPages(url, getAll());
	}

	@Override
	protected String getColumnFamily() {
		return COLUMNFAMILY;
	}
}
