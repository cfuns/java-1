package de.benjaminborbe.configuration.tools;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ConfigurationDescriptionBooleanUnitTest {

	@Test
	public void testValidate() throws Exception {
		final ConfigurationDescriptionBoolean c = new ConfigurationDescriptionBoolean(false, null, null);
		assertThat(c.validateValue("true"), is(true));
		assertThat(c.validateValue("false"), is(true));
		assertThat(c.validateValue(null), is(false));
		assertThat(c.validateValue(""), is(false));
		assertThat(c.validateValue("bla"), is(false));
	}
}
