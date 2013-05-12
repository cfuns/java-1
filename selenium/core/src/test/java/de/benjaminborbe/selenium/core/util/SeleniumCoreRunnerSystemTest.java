package de.benjaminborbe.selenium.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.mock.ConfigurationServiceMock;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.lib.test.SystemTest;
import de.benjaminborbe.selenium.core.SeleniumCoreConstatns;
import de.benjaminborbe.selenium.core.configuration.SeleniumConfigurationRegistry;
import de.benjaminborbe.selenium.core.guice.SeleniumModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(SystemTest.class)
public class SeleniumCoreRunnerSystemTest {

	@Test
	public void testRun() throws ConfigurationServiceException {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SeleniumModulesMock());
		final SeleniumCoreExecutor runner = injector.getInstance(SeleniumCoreExecutor.class);
		final SeleniumConfigurationRegistry seleniumConfigurationRegistry = injector.getInstance(SeleniumConfigurationRegistry.class);
		final ConfigurationServiceMock configurationServiceMock = injector.getInstance(ConfigurationServiceMock.class);
		final ConfigurationServiceCache configurationServiceCache = injector.getInstance(ConfigurationServiceCache.class);
		configurationServiceMock.setConfigurationValue(new ConfigurationIdentifier(SeleniumCoreConstatns.CONFIG_SELENIUM_REMOTE_HOST), "192.168.223.143");
		configurationServiceMock.setConfigurationValue(new ConfigurationIdentifier(SeleniumCoreConstatns.CONFIG_SELENIUM_REMOTE_PORT), 4444);
		configurationServiceCache.flush();

		runner.execute(seleniumConfigurationRegistry.getAll().iterator().next().getId());
	}
}
