package de.benjaminborbe.websearch.core.dao;

import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WebsearchPageDaoSubPagesAction {

	@Inject
	public WebsearchPageDaoSubPagesAction() {
	}

	public Collection<WebsearchPageBean> findSubPages(
		final String urlPrefix,
		final EntityIterator<WebsearchPageBean> entityIterator
	) throws EntityIteratorException {
		final Set<WebsearchPageBean> result = new HashSet<>();
		while (entityIterator.hasNext()) {
			final WebsearchPageBean page = entityIterator.next();
			if (page.getUrl().toExternalForm().startsWith(urlPrefix)) {
				result.add(page);
			}
		}
		return result;
	}
}
