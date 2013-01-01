package de.benjaminborbe.distributed.index.service;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.guice.DistributedIndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.map.MapChain;

public class DistributedIndexServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedIndexModulesMock());
		final DistributedIndexService distributedIndexService = injector.getInstance(DistributedIndexService.class);
		assertNotNull(distributedIndexService);
		assertEquals(DistributedIndexServiceImpl.class, distributedIndexService.getClass());
	}

	@Test
	public void testSearch() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedIndexModulesMock());
		final DistributedIndexService distributedIndexService = injector.getInstance(DistributedIndexService.class);
		{
			final DistributedIndexSearchResultIterator iterator = distributedIndexService.search(Arrays.asList("foo"));
			assertFalse(iterator.hasNext());
		}
		distributedIndexService.add(new DistributedIndexIdentifier("pageA"), new MapChain<String, Integer>().add("foo", 1));
		{
			final DistributedIndexSearchResultIterator iterator = distributedIndexService.search(Arrays.asList("foo"));
			assertTrue(iterator.hasNext());
			final DistributedIndexSearchResult result = iterator.next();
			assertEquals("pageA", result.getId().getId());
			assertEquals(new Integer(1), result.getRating());
			assertFalse(iterator.hasNext());
		}
	}
}
