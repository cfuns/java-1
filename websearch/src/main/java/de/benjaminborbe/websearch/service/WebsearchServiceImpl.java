package de.benjaminborbe.websearch.service;

import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.websearch.api.Page;
import de.benjaminborbe.websearch.api.PageIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.cron.RefreshPagesCronJob;
import de.benjaminborbe.websearch.page.PageBean;
import de.benjaminborbe.websearch.page.PageDao;

@Singleton
public class WebsearchServiceImpl implements WebsearchService {

	private final Logger logger;

	private final PageDao pageDao;

	private final RefreshPagesCronJob refreshPagesCronJob;

	@Inject
	public WebsearchServiceImpl(final Logger logger, final PageDao pageDao, final RefreshPagesCronJob refreshPagesCronJob) {
		this.logger = logger;
		this.pageDao = pageDao;
		this.refreshPagesCronJob = refreshPagesCronJob;
	}

	@Override
	public Collection<Page> getPages() throws WebsearchServiceException {
		try {
			logger.trace("getPages");
			final Collection<Page> result = new ArrayList<Page>(pageDao.getAll());
			logger.debug("found " + result.size() + " pages");
			return result;
		}
		catch (final StorageException e) {
			throw new WebsearchServiceException("StorageException", e);
		}
	}

	@Override
	public void refreshPages() {
		logger.debug("refreshPages");
		refreshPagesCronJob.execute();
	}

	@Override
	public void expirePage(final PageIdentifier pageIdentifier) throws WebsearchServiceException {
		try {
			logger.trace("expirePage");

			final PageBean page = pageDao.load(pageIdentifier.getUrl());
			if (page != null) {
				page.setLastVisit(null);
				pageDao.save(page);
			}
		}
		catch (final StorageException e) {
			throw new WebsearchServiceException("StorageException", e);
		}
	}
}
