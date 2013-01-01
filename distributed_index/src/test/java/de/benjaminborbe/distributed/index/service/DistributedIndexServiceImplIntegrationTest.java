package de.benjaminborbe.distributed.index.service;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.google.inject.Injector;

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
	public void testSearchSinglePage() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedIndexModulesMock());
		final DistributedIndexService distributedIndexService = injector.getInstance(DistributedIndexService.class);

		final String index = "defaultIndex";

		{
			final DistributedIndexSearchResultIterator iterator = distributedIndexService.search(index, Arrays.asList("foo"));
			assertFalse(iterator.hasNext());
		}
		distributedIndexService.add(index, "pageA", new MapChain<String, Integer>().add("foo", 1));
		{
			final DistributedIndexSearchResultIterator iterator = distributedIndexService.search(index, Arrays.asList("foo"));
			assertTrue(iterator.hasNext());
			final DistributedIndexSearchResult result = iterator.next();
			assertEquals("pageA", result.getId());
			assertEquals(new Integer(1), result.getRating());
			assertFalse(iterator.hasNext());
		}
	}

	@Test
	public void testSearchMultiPages() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedIndexModulesMock());
		final DistributedIndexService distributedIndexService = injector.getInstance(DistributedIndexService.class);

		final String index = "defaultIndex";

		{
			final DistributedIndexSearchResultIterator iterator = distributedIndexService.search(index, Arrays.asList("foo"));
			assertFalse(iterator.hasNext());
		}
		distributedIndexService.add(index, "pageA", new MapChain<String, Integer>().add("foo", 1));
		distributedIndexService.add(index, "pageB", new MapChain<String, Integer>().add("foo", 2).add("bar", 2));
		distributedIndexService.add(index, "pageC", new MapChain<String, Integer>().add("foo", 3).add("bar", 1));
		distributedIndexService.add(index, "pageD", new MapChain<String, Integer>().add("foo", 4));
		{
			final DistributedIndexSearchResultIterator iterator = distributedIndexService.search(index, Arrays.asList("foo"));
			{
				assertTrue(iterator.hasNext());
				final DistributedIndexSearchResult result = iterator.next();
				assertEquals("pageD", result.getId());
				assertEquals(new Integer(4), result.getRating());
			}
			{
				assertTrue(iterator.hasNext());
				final DistributedIndexSearchResult result = iterator.next();
				assertEquals("pageC", result.getId());
				assertEquals(new Integer(3), result.getRating());
			}
			{
				assertTrue(iterator.hasNext());
				final DistributedIndexSearchResult result = iterator.next();
				assertEquals("pageB", result.getId());
				assertEquals(new Integer(2), result.getRating());
			}
			{
				assertTrue(iterator.hasNext());
				final DistributedIndexSearchResult result = iterator.next();
				assertEquals("pageA", result.getId());
				assertEquals(new Integer(1), result.getRating());
			}
			{
				assertFalse(iterator.hasNext());
			}
		}

		{
			final DistributedIndexSearchResultIterator iterator = distributedIndexService.search(index, Arrays.asList("bar"));
			{
				assertTrue(iterator.hasNext());
				final DistributedIndexSearchResult result = iterator.next();
				assertEquals("pageB", result.getId());
				assertEquals(new Integer(2), result.getRating());
			}
			{
				assertTrue(iterator.hasNext());
				final DistributedIndexSearchResult result = iterator.next();
				assertEquals("pageC", result.getId());
				assertEquals(new Integer(1), result.getRating());
			}
			{
				assertFalse(iterator.hasNext());
			}
		}

	}

}
