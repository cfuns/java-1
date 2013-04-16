package de.benjaminborbe.websearch.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.core.guice.WebsearchModulesMock;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WebsearchRobotsTxtUtilIntegrationTest {

	@Test
	public void testBuildRobotsTxtUrl() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		final WebsearchRobotsTxtUtil u = injector.getInstance(WebsearchRobotsTxtUtil.class);
		assertThat(u.isAllowed("http://www.heise.de/"), is(true));
		assertThat(u.isAllowed("http://www.heise.de/bin/"), is(false));
	}
}
