package de.benjaminborbe.index.util;

import com.google.inject.Injector;

import de.benjaminborbe.index.guice.IndexGuiceInjectorBuilderMock;
import junit.framework.TestCase;

public class IdGeneratorTest extends TestCase {

	public void testNextId() {
		final Injector injector = IndexGuiceInjectorBuilderMock.getInjector();
		final IdGenerator idGenerator = injector.getInstance(IdGenerator.class);
		assertEquals(1l, idGenerator.nextId());
		assertEquals(2l, idGenerator.nextId());
		assertEquals(3l, idGenerator.nextId());
		assertEquals(4l, idGenerator.nextId());
		assertEquals(5l, idGenerator.nextId());
	}
}
