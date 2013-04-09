package de.benjaminborbe.websearch.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.guice.WebsearchModulesMock;

public class WebsearchRobotsTxtUtilIntegrationTest {

	@Test
	public void testBuildRobotsTxtUrl() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		final WebsearchRobotsTxtUtil u = injector.getInstance(WebsearchRobotsTxtUtil.class);
		assertThat(u.isAllowed("http://www.heise.de/"), is(true));
		assertThat(u.isAllowed("http://www.heise.de/bin/"), is(false));
	}
}
