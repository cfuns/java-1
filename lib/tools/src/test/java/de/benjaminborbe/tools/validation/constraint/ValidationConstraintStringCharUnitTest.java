package de.benjaminborbe.tools.validation.constraint;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ValidationConstraintStringCharUnitTest {

	@Test
	public void testValidate() throws Exception {
		{
			final ValidationConstraintStringChar v = new ValidationConstraintStringChar('-');
			assertThat(v.validate("bla"), is(false));
		}
		{
			final ValidationConstraintStringChar v = new ValidationConstraintStringChar('-');
			assertThat(v.validate("123"), is(false));
		}
		{
			final ValidationConstraintStringChar v = new ValidationConstraintStringChar('-');
			assertThat(v.validate("-"), is(true));
		}
		{
			final ValidationConstraintStringChar v = new ValidationConstraintStringChar('-');
			assertThat(v.validate("-"), is(true));
		}
	}
}
