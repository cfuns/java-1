package de.benjaminborbe.websearch.service;

import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.websearch.api.Page;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.cron.RefreshPagesCronJob;
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
	public Collection<Page> getPages() {
		logger.debug("getPages");
		return new ArrayList<Page>(pageDao.getAll());
	}

	@Override
	public void refreshPages() {
		logger.debug("refreshPages");
		refreshPagesCronJob.execute();
	}
}
