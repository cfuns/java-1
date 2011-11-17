package de.benjaminborbe.index;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.GuiceInjectorBuilderMock;
import junit.framework.TestCase;

public class IndexActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilderMock.getInjector();
		final IndexActivator o = injector.getInstance(IndexActivator.class);
		assertNotNull(o);
	}
}
