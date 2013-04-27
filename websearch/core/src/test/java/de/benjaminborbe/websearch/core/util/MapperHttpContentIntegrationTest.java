package de.benjaminborbe.websearch.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.core.guice.WebsearchModulesMock;
import org.junit.Test;

import java.net.MalformedURLException;

import static junit.framework.Assert.assertNotNull;

public class MapperHttpContentIntegrationTest {

	@Test
	public void testBuildRobotsTxtUrl() throws MalformedURLException {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		assertNotNull(injector.getInstance(MapperHttpContent.class));
	}
}
