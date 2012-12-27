package de.benjaminborbe.tools.validation.constraint;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValidationConstraintStringEmailUnitTest {

	@Test
	public void testPrecondition() throws Exception {
		final ValidationConstraintStringEmail v = new ValidationConstraintStringEmail();
		assertFalse(v.precondition(null));
		assertTrue(v.precondition("bla"));
	}

	@Test
	public void testValidate() throws Exception {
		final ValidationConstraintStringEmail v = new ValidationConstraintStringEmail();
		assertFalse(v.validate("foo"));
		assertTrue(v.validate("test@example.com"));
	}
}
