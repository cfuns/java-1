package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;

public class PageDaoSubPagesAction {

	@Inject
	public PageDaoSubPagesAction() {
	}

	public Collection<PageBean> findSubPages(final URL url, final EntityIterator<PageBean> entityIterator) throws EntityIteratorException {
		final String prefix = url.toExternalForm();
		final Set<PageBean> result = new HashSet<PageBean>();
		while (entityIterator.hasNext()) {
			final PageBean page = entityIterator.next();
			if (page.getUrl().toExternalForm().startsWith(prefix)) {
				result.add(page);
			}
		}
		return result;
	}
}
