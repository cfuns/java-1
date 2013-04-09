package de.benjaminborbe.crawler.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.CrawlerConstants;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.crawler.message.CrawlerMessage;
import de.benjaminborbe.crawler.message.CrawlerMessageMapper;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class CrawlerServiceImpl implements CrawlerService {

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final MessageService messageService;

	private final CrawlerMessageMapper crawlerMessageMapper;

	@Inject
	public CrawlerServiceImpl(final Logger logger, final ParseUtil parseUtil, final MessageService messageService, final CrawlerMessageMapper crawlerMessageMapper) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.messageService = messageService;
		this.crawlerMessageMapper = crawlerMessageMapper;
	}

	@Override
	public void processCrawlerInstruction(final CrawlerInstruction crawlerInstruction) throws CrawlerException {
		try {
			logger.trace("processCrawlerInstruction");
			final CrawlerMessage message = new CrawlerMessage(parseUtil.parseURL(crawlerInstruction.getUrl()), crawlerInstruction.getTimeout());
			messageService.sendMessage(CrawlerConstants.MESSSAGE_TYPE, crawlerInstruction.getUrl(), crawlerMessageMapper.map(message));
		}
		catch (final ParseException e) {
			throw new CrawlerException(e);
		}
		catch (final MessageServiceException e) {
			throw new CrawlerException(e);
		}
		catch (final MapException e) {
			throw new CrawlerException(e);
		}
	}

}
