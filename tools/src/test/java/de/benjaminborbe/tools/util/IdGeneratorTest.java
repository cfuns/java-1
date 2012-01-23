package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;

public class IdGeneratorTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final IdGenerator a = injector.getInstance(IdGenerator.class);
		final IdGenerator b = injector.getInstance(IdGenerator.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void NextId() {
		final IdGenerator idGenerator = new IdGeneratorImpl();
		assertEquals(1l, idGenerator.nextId());
		assertEquals(2l, idGenerator.nextId());
		assertEquals(3l, idGenerator.nextId());
		assertEquals(4l, idGenerator.nextId());
		assertEquals(5l, idGenerator.nextId());
	}
}
