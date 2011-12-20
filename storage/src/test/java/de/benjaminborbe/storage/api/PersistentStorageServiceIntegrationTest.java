package de.benjaminborbe.storage.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class PersistentStorageServiceIntegrationTest {

	@Test
	public void IsSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final PersistentStorageService o1 = injector.getInstance(PersistentStorageService.class);
		final PersistentStorageService o2 = injector.getInstance(PersistentStorageService.class);
		assertEquals(o1, o2);
		assertTrue(o1 == o2);
	}

}
