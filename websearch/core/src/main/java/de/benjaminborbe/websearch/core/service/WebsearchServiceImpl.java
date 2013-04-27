package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import de.benjaminborbe.websearch.api.WebsearchConfiguration;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.core.WebsearchConstants;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationBean;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationDao;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import de.benjaminborbe.websearch.core.util.WebsearchRefresher;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class WebsearchServiceImpl implements WebsearchService {

	private final Logger logger;

	private final WebsearchPageDao pageDao;

	private final AuthorizationService authorizationService;

	private final IndexService indexerService;

	private final CrawlerService crawlerService;

	private final DurationUtil durationUtil;

	private final WebsearchConfigurationDao websearchConfigurationDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	private final WebsearchRefresher websearchRefresher;

	@Inject
	public WebsearchServiceImpl(
		final Logger logger,
		final ParseUtil parseUtil,
		final WebsearchRefresher websearchRefresher,
		final ValidationExecutor validationExecutor,
		final WebsearchPageDao pageDao,
		final AuthorizationService authorizationService,
		final AuthenticationService authenticationService,
		final IdGeneratorUUID idGeneratorUUID,
		final IndexService indexerService,
		final CrawlerService crawlerService,
		final WebsearchConfigurationDao websearchConfigurationDao,
		final DurationUtil durationUtil
	) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.websearchRefresher = websearchRefresher;
		this.validationExecutor = validationExecutor;
		this.pageDao = pageDao;
		this.authorizationService = authorizationService;
		this.authenticationService = authenticationService;
		this.idGeneratorUUID = idGeneratorUUID;
		this.indexerService = indexerService;
		this.crawlerService = crawlerService;
		this.websearchConfigurationDao = websearchConfigurationDao;
		this.durationUtil = durationUtil;
	}

	@Override
	public Collection<WebsearchPage> getPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier("WebsearchService.getPages"));

			logger.debug("getPages");
			final EntityIterator<WebsearchPageBean> i = pageDao.getEntityIterator();
			final List<WebsearchPage> result = new ArrayList<>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			logger.debug("found " + result.size() + " pages");
			return result;
		} catch (final AuthorizationServiceException | EntityIteratorException | StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void refreshSearchIndex(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, WebsearchServiceException {
		try {
			logger.info("refreshPages");
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier("WebsearchService.refreshPages"));

			websearchRefresher.refresh();
		} catch (final AuthorizationServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void expirePage(
		final SessionIdentifier sessionIdentifier,
		final WebsearchPageIdentifier pageIdentifier
	) throws WebsearchServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier("WebsearchService.expirePage"));

			logger.debug("expirePage");

			final WebsearchPageBean page = pageDao.load(pageIdentifier);
			expirePage(page);
		} catch (final StorageException | AuthorizationServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	private void expirePage(final WebsearchPageBean page) throws StorageException {
		if (page != null) {
			page.setLastVisit(null);
			pageDao.save(page);
		}
	}

	@Override
	public void clearIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier("WebsearchService.clearIndex"));
			final String indexName = WebsearchConstants.INDEX;
			logger.debug("clearIndex - name: " + indexName);
			indexerService.clear(indexName);
		} catch (final AuthorizationServiceException | IndexerServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public WebsearchPage getPage(
		final SessionIdentifier sessionIdentifier,
		final WebsearchPageIdentifier websearchPageIdentifier
	) throws WebsearchServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier("WebsearchService.expirePage"));

			return pageDao.load(websearchPageIdentifier);
		} catch (final StorageException | AuthorizationServiceException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void refreshPage(
		final SessionIdentifier sessionIdentifier,
		final WebsearchPageIdentifier page
	) throws WebsearchServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier("WebsearchService.refreshPage"));

			final String url = page.getId();
			logger.debug("trigger refresh of url " + url);
			final CrawlerInstruction crawlerInstruction = new CrawlerInstructionBuilder(parseUtil.parseURL(url), 5000);
			crawlerService.processCrawlerInstruction(crawlerInstruction);
		} catch (final AuthorizationServiceException | CrawlerException | ParseException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public WebsearchPageIdentifier createPageIdentifier(final URL url) throws WebsearchServiceException {
		return createPageIdentifier(url != null ? url.toExternalForm() : null);
	}

	@Override
	public WebsearchPageIdentifier createPageIdentifier(final String id) throws WebsearchServiceException {
		if (id != null && !id.trim().isEmpty()) {
			return new WebsearchPageIdentifier(id);
		} else {
			return null;
		}
	}

	@Override
	public WebsearchConfigurationIdentifier createConfigurationIdentifier(final String id) throws WebsearchServiceException {
		return new WebsearchConfigurationIdentifier(id);
	}

	@Override
	public WebsearchConfiguration getConfiguration(
		final SessionIdentifier sessionIdentifier,
		final WebsearchConfigurationIdentifier websearchConfigurationIdentifier
	)
		throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("getConfiguration");

			return websearchConfigurationDao.load(websearchConfigurationIdentifier);
		} catch (final AuthorizationServiceException | StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		} finally {
			logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<WebsearchConfiguration> getConfigurations(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, LoginRequiredException,
		PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("getConfigurations");

			final EntityIterator<WebsearchConfigurationBean> i = websearchConfigurationDao.getEntityIterator();
			final List<WebsearchConfiguration> result = new ArrayList<>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final AuthorizationServiceException | EntityIteratorException | StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		} finally {
			logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteConfiguration(final SessionIdentifier sessionIdentifier, final WebsearchConfigurationIdentifier websearchConfigurationIdentifier)
		throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("deleteConfiguration");

			// delete websearch
			websearchConfigurationDao.delete(websearchConfigurationIdentifier);
		} catch (final AuthorizationServiceException | StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		} finally {
			logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public WebsearchConfigurationIdentifier createConfiguration(
		final SessionIdentifier sessionIdentifier, final URL url, final List<String> excludes, final int expire,
		final long delay, final boolean activated
	) throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("createConfiguration");

			final WebsearchConfigurationIdentifier websearchConfigurationIdentifier = createConfigurationIdentifier(idGeneratorUUID.nextId());
			final WebsearchConfigurationBean configuration = websearchConfigurationDao.create();
			configuration.setId(websearchConfigurationIdentifier);
			configuration.setUrl(url);
			configuration.setOwner(authenticationService.getCurrentUser(sessionIdentifier));
			configuration.setExcludes(excludes);
			configuration.setExpire(expire);
			configuration.setDelay(delay);
			configuration.setActivated(activated);

			final ValidationResult errors = validationExecutor.validate(configuration);
			if (errors.hasErrors()) {
				logger.warn("createConfiguration " + errors.toString());
				throw new ValidationException(errors);
			}

			websearchConfigurationDao.save(configuration);
			return websearchConfigurationIdentifier;
		} catch (final AuthorizationServiceException | AuthenticationServiceException | StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		} finally {
			logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updateConfiguration(
		final SessionIdentifier sessionIdentifier, final WebsearchConfigurationIdentifier websearchConfigurationIdentifier, final URL url,
		final List<String> excludes, final int expire, final long delay, final boolean activated
	) throws WebsearchServiceException, LoginRequiredException,
		PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("updateConfiguration");

			final WebsearchConfigurationBean configuration = websearchConfigurationDao.load(websearchConfigurationIdentifier);
			configuration.setUrl(url);
			configuration.setExcludes(excludes);
			configuration.setExpire(expire);
			configuration.setDelay(delay);
			configuration.setActivated(activated);

			final ValidationResult errors = validationExecutor.validate(configuration);
			if (errors.hasErrors()) {
				logger.warn("updateConfiguration " + errors.toString());
				throw new ValidationException(errors);
			}

			websearchConfigurationDao.save(configuration);
		} catch (final AuthorizationServiceException | StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		} finally {
			logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expireAllPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("expireAllPages");

			final EntityIterator<WebsearchPageBean> i = pageDao.getEntityIterator();
			while (i.hasNext()) {
				expirePage(i.next());
			}
		} catch (final AuthorizationServiceException | EntityIteratorException | StorageException e) {
			throw new WebsearchServiceException(e.getClass().getName(), e);
		} finally {
			logger.debug("duration " + duration.getTime());
		}
	}

}
