package de.benjaminborbe.distributed.search.service;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.guice.DistributedSearchModulesMock;
import de.benjaminborbe.distributed.search.service.DistributedSearchServiceImpl;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class DistributedSearchServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DistributedSearchModulesMock());
		final DistributedSearchService distributedIndexService = injector.getInstance(DistributedSearchService.class);
		assertNotNull(distributedIndexService);
		assertEquals(DistributedSearchServiceImpl.class, distributedIndexService.getClass());
	}

}
