package de.benjaminborbe.storage.memory.service;

import com.google.inject.Injector;
import de.benjaminborbe.storage.memory.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StorageServiceMemoryIntegrationTest {

	@org.junit.Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		assertNotNull(injector.getInstance(StorageServiceMemory.class));
	}

	@Test
	public void testSingleton() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageServiceMemory a = injector.getInstance(StorageServiceMemory.class);
		final StorageServiceMemory b = injector.getInstance(StorageServiceMemory.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}
}
