package de.benjaminborbe.search.servlet;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.json.simple.JSONArray;

import com.google.inject.Injector;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.guice.SearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SearchSuggestServletTest extends TestCase {

	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchModulesMock());
		final SearchSuggestServlet a = injector.getInstance(SearchSuggestServlet.class);
		final SearchSuggestServlet b = injector.getInstance(SearchSuggestServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	public void testBuildJson() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchModulesMock());
		final SearchSuggestServlet searchServlet = injector.getInstance(SearchSuggestServlet.class);
		final List<SearchResult> bookmarks = new ArrayList<SearchResult>();
		final SearchResult searchResult = EasyMock.createMock(SearchResult.class);
		EasyMock.expect(searchResult.getUrl()).andReturn(new URL("http://www.google.com"));
		EasyMock.expect(searchResult.getTitle()).andReturn("Google");
		EasyMock.replay(searchResult);
		bookmarks.add(searchResult);
		final JSONArray json = searchServlet.buildJson(bookmarks);
		assertNotNull(json);
		final StringWriter out = new StringWriter();
		json.writeJSONString(out);
		assertEquals("[\"Google\"]", out.toString());
	}
}
