package de.benjaminborbe.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IdentifierBaseUnitTest {

	@Test
	public void testEquals() throws Exception {
		final TestIdentifier a1 = new TestIdentifier("a");
		final TestIdentifier a2 = new TestIdentifier("a");
		final TestIdentifier b1 = new TestIdentifier("b");
		assertEquals(a1, a1);
		assertEquals(a1, a2);
		assertNotSame(a1, b1);
	}

	@Test
	public void testHashCode() {
		final TestIdentifier a1 = new TestIdentifier("a");
		final TestIdentifier a2 = new TestIdentifier("a");
		final TestIdentifier b1 = new TestIdentifier("b");
		assertTrue(a1.hashCode() == a1.hashCode());
		assertTrue(a1.hashCode() == a2.hashCode());
		assertFalse(a1.hashCode() == b1.hashCode());
	}

}
