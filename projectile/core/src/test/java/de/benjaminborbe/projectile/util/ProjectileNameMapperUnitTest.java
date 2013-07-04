package de.benjaminborbe.projectile.util;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProjectileNameMapperUnitTest {

	@Test
	public void testConvertName() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ProjectileNameMapper mapper = new ProjectileNameMapper(logger);
		assertThat(mapper.fullnameToLogin("bgates"), is("bgates"));
		assertThat(mapper.fullnameToLogin("Foo Bar"), is("bfoo"));
		assertThat(mapper.fullnameToLogin("van Foo Bar"), is("bfoo"));
		assertThat(mapper.fullnameToLogin("Foo v b a Bar"), is("bfoo"));
		assertThat(mapper.fullnameToLogin("Becker Söhn Karl"), is("kbecker"));
	}
}
