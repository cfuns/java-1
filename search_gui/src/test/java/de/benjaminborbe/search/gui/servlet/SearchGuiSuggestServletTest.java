package de.benjaminborbe.search.gui.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.json.simple.JSONArray;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.gui.guice.SearchGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SearchGuiSuggestServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchGuiModulesMock());
		final SearchGuiSuggestServlet a = injector.getInstance(SearchGuiSuggestServlet.class);
		final SearchGuiSuggestServlet b = injector.getInstance(SearchGuiSuggestServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void BuildJson() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SearchGuiModulesMock());
		final SearchGuiSuggestServlet searchServlet = injector.getInstance(SearchGuiSuggestServlet.class);
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
