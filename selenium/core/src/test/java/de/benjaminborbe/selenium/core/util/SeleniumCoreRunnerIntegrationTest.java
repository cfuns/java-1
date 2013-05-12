package de.benjaminborbe.selenium.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.selenium.core.guice.SeleniumModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SeleniumCoreRunnerIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SeleniumModulesMock());
		assertThat(injector.getInstance(SeleniumCoreRunner.class), is(notNullValue()));
	}

}
