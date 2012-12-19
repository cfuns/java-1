package de.benjaminborbe.index.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class IndexIntegrationTest {

	private static final String INDEXNAME = "test";

	@Test
	public void testInjections() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);
	}

	@Test
	public void testSearchLike() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de/a"), "title", "content");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "titl*").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "content").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "conten*").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de/b"), "aaa bbb ccc", "ddd eee fff");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "aaa").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "aa*").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "bbb").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "bb*").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "ccc").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "cc*").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "ddd").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "dd*").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "eee").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "ee*").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "fff").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "ff*").size());
	}

	@Test
	public void testUniqueUrl() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "title", "content");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "title", "content");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de/index.html"), "title", "content");
		assertEquals(2, indexSearcherService.search(INDEXNAME, "title").size());
	}

	@Test
	public void testIindexAndSearch() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title").size());
		assertEquals(0, indexSearcherService.search(INDEXNAME, "content").size());
		assertEquals(0, indexSearcherService.search(INDEXNAME, "http://test.de").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "title", "content");

		assertEquals(1, indexSearcherService.search(INDEXNAME, "title").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "content").size());
		assertEquals(1, indexSearcherService.search(INDEXNAME, "http://test.de").size());
	}

	@Test
	public void testClear() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "title", "contentA");
		assertTrue(indexSearcherService.search(INDEXNAME, "title").size() > 0);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "title", "contentA");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title").size());
	}
}
