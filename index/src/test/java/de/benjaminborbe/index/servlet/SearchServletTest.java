package de.benjaminborbe.index.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.google.inject.Injector;

import de.benjaminborbe.index.dao.Bookmark;
import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import junit.framework.TestCase;

public class SearchServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final SearchServlet a = injector.getInstance(SearchServlet.class);
		final SearchServlet b = injector.getInstance(SearchServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	public void testBuildJson() throws IOException {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final SearchServlet searchServlet = injector.getInstance(SearchServlet.class);
		final List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		final Bookmark bookmark = new Bookmark();
		bookmark.setId(1l);
		bookmark.setUrl("http://www.google.com");
		bookmark.setName("Google");
		bookmarks.add(bookmark);
		final JSONArray json = searchServlet.buildJson(bookmarks);
		assertNotNull(json);
		final StringWriter out = new StringWriter();
		json.writeJSONString(out);
		assertEquals("[\"Google\"]", out.toString());
	}
}
