package de.benjaminborbe.index.service;

import java.net.URL;
import java.util.List;

import com.google.inject.Injector;

import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import junit.framework.TestCase;

public class IndexIntegrationTest extends TestCase {

	public void testInjections() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);
	}

	public void testIndexAndSearch() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);
		final String indexName = "test";
		indexerService.addToIndex(indexName, new URL("http://test.de"), "titleA", "contentB");
		final List<IndexSearchResult> results = indexSearcherService.search(indexName, "title*");
		assertNotNull(results);
		assertEquals(1, results.size());
	}
}
