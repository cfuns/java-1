package de.benjaminborbe.bookmark.service;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.tools.search.SearchUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.net.MalformedURLException;

import static org.junit.Assert.assertNotNull;

public class BookmarkSearchServiceComponentUnitTest {

	@Test
	public void testBuildUrl() throws MalformedURLException {
		final Logger logger = EasyMock.createMock(Logger.class);
		EasyMock.replay(logger);

		final BookmarkService bookmarkService = EasyMock.createMock(BookmarkService.class);
		EasyMock.replay(bookmarkService);

		final SearchUtil searchUtil = null;
		final BookmarkSearchServiceComponent bookmarkSearchService = new BookmarkSearchServiceComponent(logger, bookmarkService, searchUtil);
		assertNotNull(bookmarkSearchService);
	}
}
