package de.benjaminborbe.index.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class IndexIntegrationTest {

	private static final String INDEXNAME = "test";

	private static int SEARCH_LIMIT = 10;

	@Test
	public void testInjections() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexService indexerService = injector.getInstance(IndexService.class);
		assertNotNull(indexerService);
		final IndexService indexSearcherService = injector.getInstance(IndexService.class);
		assertNotNull(indexSearcherService);
	}

	@Test
	public void testSearchLike() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexService indexerService = injector.getInstance(IndexService.class);
		assertNotNull(indexerService);
		final IndexService indexSearcherService = injector.getInstance(IndexService.class);
		assertNotNull(indexSearcherService);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());

		indexerService.addToIndex(INDEXNAME, new URL("http://example.com/a"), "title", "content");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "titl*", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "content", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "conten*", SEARCH_LIMIT).size());

		indexerService.addToIndex(INDEXNAME, new URL("http://example.com/b"), "aaa bbb ccc", "ddd eee fff");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "aaa", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "aa*", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "bbb", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "bb*", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "ccc", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "cc*", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "ddd", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "dd*", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "eee", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "ee*", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "fff", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "ff*", SEARCH_LIMIT).size());
	}

	@Test
	public void testUniqueUrl() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexService indexerService = injector.getInstance(IndexService.class);
		assertNotNull(indexerService);
		final IndexService indexSearcherService = injector.getInstance(IndexService.class);
		assertNotNull(indexSearcherService);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());

		indexerService.addToIndex(INDEXNAME, new URL("http://example.com"), "title", "content");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());

		indexerService.addToIndex(INDEXNAME, new URL("http://example.com"), "title", "content");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());

		indexerService.addToIndex(INDEXNAME, new URL("http://example.com/index.html"), "title", "content");
		assertEquals(2, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());
	}

	@Test
	public void testIindexAndSearch() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexService indexerService = injector.getInstance(IndexService.class);
		assertNotNull(indexerService);
		final IndexService indexSearcherService = injector.getInstance(IndexService.class);
		assertNotNull(indexSearcherService);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());
		assertEquals(0, indexSearcherService.search(INDEXNAME, "content", SEARCH_LIMIT).size());
		assertEquals(0, indexSearcherService.search(INDEXNAME, "http://example.com", SEARCH_LIMIT).size());

		indexerService.addToIndex(INDEXNAME, new URL("http://example.com"), "title", "content");

		assertEquals(1, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "content", SEARCH_LIMIT).size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "http://example.com", SEARCH_LIMIT).size());
	}

	@Test
	public void testClear() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexService indexerService = injector.getInstance(IndexService.class);
		assertNotNull(indexerService);
		final IndexService indexSearcherService = injector.getInstance(IndexService.class);
		assertNotNull(indexSearcherService);

		indexerService.addToIndex(INDEXNAME, new URL("http://example.com"), "title", "contentA");
		assertTrue(indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size() > 0);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());

		indexerService.addToIndex(INDEXNAME, new URL("http://example.com"), "title", "contentA");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title", SEARCH_LIMIT).size());
	}
}
