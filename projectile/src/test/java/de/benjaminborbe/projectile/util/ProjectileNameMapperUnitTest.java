package de.benjaminborbe.projectile.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ProjectileNameMapperUnitTest {

	@Test
	public void testConvertName() throws Exception {
		final ProjectileNameMapper mapper = new ProjectileNameMapper();
		assertThat(mapper.fullnameToLogin("Foo Bar"), is("bfoo"));
		assertThat(mapper.fullnameToLogin("van Foo Bar"), is("bfoo"));
		assertThat(mapper.fullnameToLogin("Foo v b a Bar"), is("bfoo"));
	}
}
