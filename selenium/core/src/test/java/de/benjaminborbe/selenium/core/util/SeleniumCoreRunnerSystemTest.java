package de.benjaminborbe.selenium.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.lib.test.SystemTest;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import de.benjaminborbe.selenium.core.guice.SeleniumModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(SystemTest.class)
public class SeleniumCoreRunnerSystemTest {

	@Test
	public void testRun() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SeleniumModulesMock());
		final SeleniumCoreExecutor runner = injector.getInstance(SeleniumCoreExecutor.class);
		SeleniumConfigurationRegistry seleniumConfigurationRegistry = injector.getInstance(SeleniumConfigurationRegistry.class);
		runner.execute(seleniumConfigurationRegistry.getAll().iterator().next().getId());
	}
}
