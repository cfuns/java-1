package de.benjaminborbe.distributed.search.service;

import com.google.inject.Injector;
import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.dao.DistributedSearchPageDao;
import de.benjaminborbe.distributed.search.guice.DistributedSearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DistributedSearchServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedSearchModulesMock());
		final DistributedSearchService distributedIndexService = injector.getInstance(DistributedSearchService.class);
		assertNotNull(distributedIndexService);
		assertEquals(DistributedSearchServiceImpl.class, distributedIndexService.getClass());
	}

	@Test
	public void testClear() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedSearchModulesMock());
		final DistributedSearchService distributedIndexService = injector.getInstance(DistributedSearchService.class);
		final DistributedSearchPageDao dao = injector.getInstance(DistributedSearchPageDao.class);

		final String index = "defaultIndex";
		final URL url = new URL("http://www.example.com/b");
		final String title = "title";
		final String content = "content";

		assertFalse(dao.getIdentifierIteratorByIndex(index).hasNext());
		distributedIndexService.addToIndex(index, url, title, content, null);
		assertTrue(dao.getIdentifierIteratorByIndex(index).hasNext());
		distributedIndexService.clear(index);
		assertFalse(dao.getIdentifierIteratorByIndex(index).hasNext());
	}
}
