package de.benjaminborbe.task.api;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaskIdentifierTest {

	@Test
	public void testEquals() {
		assertTrue(new TaskIdentifier("a").equals(new TaskIdentifier("a")));
		assertFalse(new TaskIdentifier("a").equals(new TaskIdentifier("b")));
	}
}
