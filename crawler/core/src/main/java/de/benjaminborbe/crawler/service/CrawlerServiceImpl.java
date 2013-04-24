package de.benjaminborbe.crawler.service;

import de.benjaminborbe.crawler.CrawlerConstants;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
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

	@Inject
	public CrawlerServiceImpl(final Logger logger, final MessageService messageService, final CrawlerMessageMapper crawlerMessageMapper) {
		this.logger = logger;
		this.messageService = messageService;
		this.crawlerMessageMapper = crawlerMessageMapper;
	}

	@Override
	public void processCrawlerInstruction(final CrawlerInstruction crawlerInstruction) throws CrawlerException {
		try {
			logger.trace("processCrawlerInstruction");
			final CrawlerMessage message = new CrawlerMessage(crawlerInstruction.getUrl(), crawlerInstruction.getTimeout());
			messageService.sendMessage(CrawlerConstants.MESSSAGE_TYPE, String.valueOf(crawlerInstruction.getUrl()), crawlerMessageMapper.map(message));
		} catch (final MapException | MessageServiceException e) {
			throw new CrawlerException(e);
		}
	}

}
