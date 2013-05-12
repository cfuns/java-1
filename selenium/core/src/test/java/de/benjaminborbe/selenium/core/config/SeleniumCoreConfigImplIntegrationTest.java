package de.benjaminborbe.selenium.core.config;

import com.google.inject.Injector;
import de.benjaminborbe.selenium.core.guice.SeleniumModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SeleniumCoreConfigImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SeleniumModulesMock());
		assertThat(injector.getInstance(SeleniumCoreConfigImpl.class), is(notNullValue()));
		assertThat(injector.getInstance(SeleniumCoreConfigImpl.class).getClass().getName(), is(SeleniumCoreConfigImpl.class.getName()));
	}
}
