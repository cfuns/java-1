package de.benjaminborbe.storage.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CacheStorageServiceIntegrationTest {

	@Test
	public void IsSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final CacheStorageService o1 = injector.getInstance(CacheStorageService.class);
		final CacheStorageService o2 = injector.getInstance(CacheStorageService.class);
		assertEquals(o1, o2);
		assertTrue(o1 == o2);
	}
}
