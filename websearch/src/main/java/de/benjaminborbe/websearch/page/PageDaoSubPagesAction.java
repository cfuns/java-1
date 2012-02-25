package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

public class PageDaoSubPagesAction {

	@Inject
	public PageDaoSubPagesAction() {
	}

	public Collection<PageBean> findSubPages(final URL url, final Collection<PageBean> pages) {
		final String prefix = url.toExternalForm();
		final Set<PageBean> result = new HashSet<PageBean>();
		for (final PageBean page : pages) {
			if (page.getUrl().toExternalForm().startsWith(prefix)) {
				result.add(page);
			}
		}
		return result;
	}
}
