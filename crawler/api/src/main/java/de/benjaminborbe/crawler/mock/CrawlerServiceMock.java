package de.benjaminborbe.crawler.mock;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
import de.benjaminborbe.crawler.api.CrawlerService;

public class CrawlerServiceMock implements CrawlerService {

	@Override
	public void processCrawlerInstruction(final CrawlerInstruction crawlerInstruction) throws CrawlerException {
	}

	@Override
	public void notify(final CrawlerNotifierResult httpResponse) throws CrawlerException {
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier) throws CrawlerException, PermissionDeniedException, LoginRequiredException {
	}

	@Override
	public boolean hasPermission(final SessionIdentifier sessionIdentifier) throws CrawlerException {
		return true;
	}

}
