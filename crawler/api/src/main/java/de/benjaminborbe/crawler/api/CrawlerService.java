package de.benjaminborbe.crawler.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface CrawlerService {

	void processCrawlerInstruction(CrawlerInstruction crawlerInstruction) throws CrawlerException;

	void notify(CrawlerNotifierResult httpResponse) throws CrawlerException;

	void expectPermission(SessionIdentifier sessionIdentifier) throws CrawlerException, PermissionDeniedException, LoginRequiredException;

	boolean hasPermission(SessionIdentifier sessionIdentifier) throws CrawlerException;
}
