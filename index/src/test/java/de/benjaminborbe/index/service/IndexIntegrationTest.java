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
	public void testIindexAndSearch() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title*").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "titleA", "contentA");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title*").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "titleB", "contentB");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title*").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de/index.html"), "titleC", "contentC");
		assertEquals(2, indexSearcherService.search(INDEXNAME, "title*").size());
	}

	@Test
	public void testClear() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "titleA", "contentA");
		assertTrue(indexSearcherService.search(INDEXNAME, "title*").size() > 0);

		indexerService.clear(INDEXNAME);
		assertEquals(0, indexSearcherService.search(INDEXNAME, "title*").size());

		indexerService.addToIndex(INDEXNAME, new URL("http://test.de"), "titleA", "contentA");
		assertEquals(1, indexSearcherService.search(INDEXNAME, "title*").size());
	}
}
