package de.benjaminborbe.index;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import junit.framework.TestCase;

public class IndexActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new IndexModulesMock());
		final IndexActivator o = injector.getInstance(IndexActivator.class);
		assertNotNull(o);
	}
}
