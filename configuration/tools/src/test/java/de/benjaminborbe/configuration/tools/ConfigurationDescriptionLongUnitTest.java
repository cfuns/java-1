package de.benjaminborbe.configuration.tools;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ConfigurationDescriptionLongUnitTest {

	@Test
	public void testValidate() throws Exception {
		final ConfigurationDescriptionLong c = new ConfigurationDescriptionLong(123, null, null);
		assertThat(c.validateValue("1"), is(true));
		assertThat(c.validateValue("0"), is(true));
		assertThat(c.validateValue("-1"), is(true));
		assertThat(c.validateValue(null), is(false));
		assertThat(c.validateValue(""), is(false));
		assertThat(c.validateValue("bla"), is(false));
	}
}
