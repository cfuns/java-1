package de.benjaminborbe.crawler.service;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.crawler.CrawlerConstants;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.message.CrawlerMessage;
import de.benjaminborbe.crawler.message.CrawlerMessageMapper;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.tools.mapper.MapException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CrawlerServiceImpl implements CrawlerService {

	private final Logger logger;

	private final MessageService messageService;

	private final CrawlerMessageMapper crawlerMessageMapper;

	private final CrawlerNotifier crawlerNotifier;

	private final AuthorizationService authorizationService;

	@Inject
	public CrawlerServiceImpl(
		final Logger logger,
		final MessageService messageService,
		final CrawlerMessageMapper crawlerMessageMapper,
		final CrawlerNotifier crawlerNotifier,
		final AuthorizationService authorizationService
	) {
		this.logger = logger;
		this.messageService = messageService;
		this.crawlerMessageMapper = crawlerMessageMapper;
		this.crawlerNotifier = crawlerNotifier;
		this.authorizationService = authorizationService;
	}

	@Override
	public void processCrawlerInstruction(final CrawlerInstruction crawlerInstruction) throws CrawlerException {
		try {
			logger.trace("processCrawlerInstruction");
			final CrawlerMessage message = new CrawlerMessage(crawlerInstruction.getUrl(), crawlerInstruction.getDepth(), crawlerInstruction.getTimeout());
			messageService.sendMessage(CrawlerConstants.MESSSAGE_TYPE, String.valueOf(crawlerInstruction.getUrl()), crawlerMessageMapper.map(message));
		} catch (final MapException | MessageServiceException e) {
			throw new CrawlerException(e);
		}
	}

	@Override
	public void notify(final CrawlerNotifierResult httpResponse) throws CrawlerException {
		try {
			logger.debug("notify " + httpResponse.getUrl());
			crawlerNotifier.notifiy(httpResponse);
		} finally {

		}
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier) throws CrawlerException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(CrawlerConstants.PERMISSON_NAME));
		} catch (AuthorizationServiceException e) {
			throw new CrawlerException(e);
		}
	}

	@Override
	public boolean hasPermission(final SessionIdentifier sessionIdentifier) throws CrawlerException {
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(CrawlerConstants.PERMISSON_NAME));
		} catch (AuthorizationServiceException e) {
			throw new CrawlerException(e);
		}
	}

}
