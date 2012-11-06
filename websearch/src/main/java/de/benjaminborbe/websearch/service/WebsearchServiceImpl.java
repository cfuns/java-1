package de.benjaminborbe.websearch.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.websearch.WebsearchActivator;
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

	private final AuthorizationService authorizationService;

	private final IndexerService indexerService;

	@Inject
	public WebsearchServiceImpl(
			final Logger logger,
			final PageDao pageDao,
			final RefreshPagesCronJob refreshPagesCronJob,
			final AuthorizationService authorizationService,
			final IndexerService indexerService) {
		this.logger = logger;
		this.pageDao = pageDao;
		this.refreshPagesCronJob = refreshPagesCronJob;
		this.authorizationService = authorizationService;
		this.indexerService = indexerService;
	}

	@Override
	public Collection<Page> getPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("WebsearchService.getPages"));

			logger.debug("getPages");
			final EntityIterator<PageBean> i = pageDao.getIterator();
			final List<Page> result = new ArrayList<Page>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			logger.debug("found " + result.size() + " pages");
			return result;
		}
		catch (final AuthorizationServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
		catch (final StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
		catch (final EntityIteratorException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void refreshPages(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, WebsearchServiceException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("WebsearchService.refreshPages"));

			logger.debug("refreshPages");
			refreshPagesCronJob.execute();
		}
		catch (final AuthorizationServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void expirePage(final SessionIdentifier sessionIdentifier, final PageIdentifier pageIdentifier) throws WebsearchServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("WebsearchService.expirePage"));

			logger.debug("expirePage");

			final PageBean page = pageDao.load(pageIdentifier.getUrl());
			if (page != null) {
				page.setLastVisit(null);
				pageDao.save(page);
			}
		}
		catch (final StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
		catch (final AuthorizationServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void clearIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("WebsearchService.clearIndex"));

			final String indexName = WebsearchActivator.INDEX;
			indexerService.clear(indexName);
		}
		catch (final AuthorizationServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
		catch (final IndexerServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}
}
