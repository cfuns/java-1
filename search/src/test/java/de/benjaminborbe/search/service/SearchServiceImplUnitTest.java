package de.benjaminborbe.search.service;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.dao.SearchQueryHistoryDao;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerMock;

public class SearchServiceImplUnitTest {

	@Test
	public void testSearchSpecial() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final SessionIdentifier sessionIdentifier = new SessionIdentifier("sessionId");
		final int maxResults = 1337;

		final SearchQueryHistoryDao searchQueryHistoryDao = null;
		final IdGeneratorUUID idGeneratorUUID = null;

		final List<SearchResult> list = Arrays.asList();
		final SearchServiceComponent searchServiceComponent = EasyMock.createMock(SearchServiceComponent.class);
		EasyMock.expect(searchServiceComponent.search(sessionIdentifier, "bar", maxResults)).andReturn(list);
		EasyMock.replay(searchServiceComponent);

		final SearchServiceComponentRegistry searchServiceComponentRegistry = EasyMock.createNiceMock(SearchServiceComponentRegistry.class);
		EasyMock.expect(searchServiceComponentRegistry.get("foo")).andReturn(searchServiceComponent);
		EasyMock.replay(searchServiceComponentRegistry);

		final ThreadRunner threadRunner = new ThreadRunnerMock();

		final SearchServiceSearchResultComparator searchServiceComponentComparator = null;

		final SearchService searchService = new SearchServiceImpl(logger, searchQueryHistoryDao, idGeneratorUUID, searchServiceComponentRegistry, threadRunner,
				searchServiceComponentComparator);
		searchService.search(sessionIdentifier, "foo: bar", maxResults);

		EasyMock.verify(searchServiceComponentRegistry, searchServiceComponent);

	}
}