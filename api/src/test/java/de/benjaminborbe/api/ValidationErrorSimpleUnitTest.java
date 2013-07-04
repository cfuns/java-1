package de.benjaminborbe.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValidationErrorSimpleUnitTest {

	@Test
	public void testEquals() throws Exception {
		assertEquals(new ValidationErrorSimple("a"), new ValidationErrorSimple("a"));
	}
}
