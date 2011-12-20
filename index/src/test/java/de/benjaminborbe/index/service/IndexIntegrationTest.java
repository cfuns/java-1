package de.benjaminborbe.index.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class IndexIntegrationTest {

	@Test
	public void Injections() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexerService indexerService = injector.getInstance(IndexerService.class);
		assertNotNull(indexerService);
		final IndexSearcherService indexSearcherService = injector.getInstance(IndexSearcherService.class);
		assertNotNull(indexSearcherService);
	}

	@Test
	public void IndexAndSearch() throws Exception {
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
