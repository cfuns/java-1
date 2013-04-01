package de.benjaminborbe.websearch.dao;

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

	public Collection<WebsearchPageBean> findSubPages(final String urlPrefix, final EntityIterator<WebsearchPageBean> entityIterator) throws EntityIteratorException {
		final Set<WebsearchPageBean> result = new HashSet<WebsearchPageBean>();
		while (entityIterator.hasNext()) {
			final WebsearchPageBean page = entityIterator.next();
			if (page.getUrl().startsWith(urlPrefix)) {
				result.add(page);
			}
		}
		return result;
	}
}
