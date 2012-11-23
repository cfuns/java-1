package de.benjaminborbe.websearch.page;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;

public class WebsearchPageDaoSubPagesAction {

	@Inject
	public WebsearchPageDaoSubPagesAction() {
	}

	public Collection<WebsearchPageBean> findSubPages(final URL url, final EntityIterator<WebsearchPageBean> entityIterator) throws EntityIteratorException {
		final String prefix = url.toExternalForm();
		final Set<WebsearchPageBean> result = new HashSet<WebsearchPageBean>();
		while (entityIterator.hasNext()) {
			final WebsearchPageBean page = entityIterator.next();
			if (page.getUrl().toExternalForm().startsWith(prefix)) {
				result.add(page);
			}
		}
		return result;
	}
}
