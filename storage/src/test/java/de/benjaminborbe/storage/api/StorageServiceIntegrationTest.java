package de.benjaminborbe.storage.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageServiceIntegrationTest {

	@Test
	public void testIsSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageService o1 = injector.getInstance(StorageService.class);
		final StorageService o2 = injector.getInstance(StorageService.class);
		assertEquals(o1, o2);
		assertTrue(o1 == o2);
	}

}
