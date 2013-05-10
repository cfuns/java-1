package de.benjaminborbe.lib.validation.constraint;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
		assertThat(v.validate("foo"), is(false));
		assertThat(v.validate("test@example.com"), is(true));
		assertThat(v.validate("test@localhost"), is(true));
		assertThat(v.validate("a@b"), is(true));
		assertThat(v.validate("hello @ world"), is(false));
	}
}
