package de.benjaminborbe.tools.validation.constraint;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ValidationConstraintOrUnitTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testValidate() throws Exception {
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>();
			assertThat(v.validate("bla"), is(false));
		}
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>(new ValidationConstraintStringOnlyDigest());
			assertThat(v.validate("bla"), is(false));
		}
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>(new ValidationConstraintStringOnlyLetters());
			assertThat(v.validate("bla"), is(true));
		}
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>(new ValidationConstraintStringOnlyLetters(), new ValidationConstraintStringOnlyDigest());
			assertThat(v.validate("bla"), is(true));
		}
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>(new ValidationConstraintStringOnlyLetters(), new ValidationConstraintStringChar('-'));
			assertThat(v.validate("-"), is(true));
		}
	}
}
