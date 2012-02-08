package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.dao.DaoCache;

@Singleton
public class PageDaoImpl extends DaoCache<PageBean, URL> implements PageDao {

	@Inject
	public PageDaoImpl(final Logger logger, final Provider<PageBean> provider) {
		super(logger, provider);
	}

	@Override
	public Collection<PageBean> findExpiredPages() {
		return new HashSet<PageBean>();
	}

	@Override
	public Collection<PageBean> findSubPages(final URL url) {
		final String prefix = url.toExternalForm();
		final Set<PageBean> result = new HashSet<PageBean>();
		for (final PageBean page : getAll()) {
			if (page.getId().toExternalForm().startsWith(prefix)) {
				result.add(page);
			}
		}
		return result;
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
			page.setId(url);
			save(page);
			return page;
		}
	}

}
