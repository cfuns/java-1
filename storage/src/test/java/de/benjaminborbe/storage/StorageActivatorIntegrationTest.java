package de.benjaminborbe.storage;

import com.google.inject.Injector;

import junit.framework.TestCase;
import de.benjaminborbe.storage.StorageActivator;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageActivatorIntegrationTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageActivator o = injector.getInstance(StorageActivator.class);
		assertNotNull(o);
	}
}
