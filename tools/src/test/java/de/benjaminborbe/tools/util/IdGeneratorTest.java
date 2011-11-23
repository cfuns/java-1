package de.benjaminborbe.tools.util;

import de.benjaminborbe.tools.util.IdGenerator;
import junit.framework.TestCase;

public class IdGeneratorTest extends TestCase {

	public void testNextId() {
		final IdGenerator idGenerator = new IdGeneratorImpl();
		assertEquals(1l, idGenerator.nextId());
		assertEquals(2l, idGenerator.nextId());
		assertEquals(3l, idGenerator.nextId());
		assertEquals(4l, idGenerator.nextId());
		assertEquals(5l, idGenerator.nextId());
	}
}
