package de.benjaminborbe.bookmark.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.easymock.EasyMock;
import org.slf4j.Logger;

import de.benjaminborbe.bookmark.api.BookmarkService;

import junit.framework.TestCase;

public class BookmarkSearchServiceTest extends TestCase {

	public void testBuildUrl() throws MalformedURLException {
		final Logger logger = EasyMock.createMock(Logger.class);
		EasyMock.replay(logger);
		final BookmarkService bookmarkService = EasyMock.createMock(BookmarkService.class);
		EasyMock.replay(bookmarkService);
		final BookmarkSearchService bookmarkSearchService = new BookmarkSearchService(logger, bookmarkService);
		assertEquals(new URL("http://www.heise.de"), bookmarkSearchService.buildUrl("http://www.heise.de"));
		assertEquals(new URL("http://bb/bla"), bookmarkSearchService.buildUrl("/bla"));
	}
}
