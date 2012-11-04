package de.benjaminborbe.task.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TaskIdentifierTest {

	@Test
	public void testEquals() {
		assertTrue(new TaskIdentifier("a").equals(new TaskIdentifier("a")));
		assertFalse(new TaskIdentifier("a").equals(new TaskIdentifier("b")));
	}
}
