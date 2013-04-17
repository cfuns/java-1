package de.benjaminborbe.tools.validation.constraint;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValidationConstraintOrUnitTest {

	@Test
	public void testValidate() throws Exception {
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<>();
			assertThat(v.validate("bla"), is(false));
		}
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>().add(new ValidationConstraintStringOnlyDigest());
			assertThat(v.validate("bla"), is(false));
		}
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>().add(new ValidationConstraintStringOnlyLetters());
			assertThat(v.validate("bla"), is(true));
		}
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>().add(new ValidationConstraintStringOnlyLetters())
				.add(new ValidationConstraintStringOnlyDigest());
			assertThat(v.validate("bla"), is(true));
		}
		{
			final ValidationConstraintOr<String> v = new ValidationConstraintOr<String>().add(new ValidationConstraintStringOnlyLetters()).add(new ValidationConstraintStringChar('-'));
			assertThat(v.validate("-"), is(true));
		}
	}
}
