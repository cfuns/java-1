package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IdGeneratorTest {

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
