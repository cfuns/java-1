package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IdGeneratorUnitTest {

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
