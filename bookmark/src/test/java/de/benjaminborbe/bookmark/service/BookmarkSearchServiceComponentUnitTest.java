package de.benjaminborbe.bookmark.service;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.bookmark.api.BookmarkService;

public class BookmarkSearchServiceComponentUnitTest {

	@Test
	public void testBuildUrl() throws MalformedURLException {
		final Logger logger = EasyMock.createMock(Logger.class);
		EasyMock.replay(logger);

		final BookmarkService bookmarkService = EasyMock.createMock(BookmarkService.class);
		EasyMock.replay(bookmarkService);

		final BookmarkSearchServiceComponent bookmarkSearchService = new BookmarkSearchServiceComponent(logger, bookmarkService);
		assertNotNull(bookmarkSearchService);
	}
}
