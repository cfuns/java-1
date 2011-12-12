package de.benjaminborbe.bookmark.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.json.simple.JSONArray;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.bookmark.servlet.BookmarkSearchServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkSearchServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkSearchServlet a = injector.getInstance(BookmarkSearchServlet.class);
		final BookmarkSearchServlet b = injector.getInstance(BookmarkSearchServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	public void testBuildJson() throws IOException {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkSearchServlet searchServlet = injector.getInstance(BookmarkSearchServlet.class);
		final List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		final Bookmark bookmark = EasyMock.createMock(Bookmark.class);
		EasyMock.expect(bookmark.getUrl()).andReturn("http://www.google.com");
		EasyMock.expect(bookmark.getName()).andReturn("Google");
		EasyMock.replay(bookmark);
		bookmarks.add(bookmark);
		final JSONArray json = searchServlet.buildJson(bookmarks);
		assertNotNull(json);
		final StringWriter out = new StringWriter();
		json.writeJSONString(out);
		assertEquals("[\"Google\"]", out.toString());
	}
}
