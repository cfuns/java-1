package de.benjaminborbe.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValidationErrorSimpleUnitTest {

	@Test
	public void testEquals() throws Exception {
		assertEquals(new ValidationErrorSimple("a"), new ValidationErrorSimple("a"));
	}
}
