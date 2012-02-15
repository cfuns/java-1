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
		final IdGeneratorLong a = injector.getInstance(IdGeneratorLong.class);
		final IdGeneratorLong b = injector.getInstance(IdGeneratorLong.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void testNextId() {
		final IdGeneratorLong idGenerator = new IdGeneratorLongImpl();
		assertEquals(new Long(1), idGenerator.nextId());
		assertEquals(new Long(2), idGenerator.nextId());
		assertEquals(new Long(3), idGenerator.nextId());
		assertEquals(new Long(4), idGenerator.nextId());
		assertEquals(new Long(5), idGenerator.nextId());
	}
}
