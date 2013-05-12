package de.benjaminborbe.selenium.core.action;

import com.google.inject.Injector;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationClick;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectText;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationGetUrl;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationPageContent;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationPageInfo;
import de.benjaminborbe.selenium.core.guice.SeleniumModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SeleniumActionRegistryImplIntegrationTest {

	@Test
	public void testInject() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SeleniumModulesMock());
		assertThat(injector.getInstance(SeleniumActionRegistry.class), is(notNullValue()));
		assertThat(injector.getInstance(SeleniumActionRegistry.class).getClass().getName(), is(SeleniumActionRegistryImpl.class.getName()));
	}

	@Test
	public void testRegistered() throws MalformedURLException {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SeleniumModulesMock());
		final SeleniumActionRegistry registry = injector.getInstance(SeleniumActionRegistry.class);

		assertThat(registry.get(SeleniumActionConfigurationClick.class), is(notNullValue()));
		assertThat(registry.get(SeleniumActionConfigurationExpectText.class), is(notNullValue()));
		assertThat(registry.get(SeleniumActionConfigurationGetUrl.class), is(notNullValue()));
		assertThat(registry.get(SeleniumActionConfigurationPageContent.class), is(notNullValue()));
		assertThat(registry.get(SeleniumActionConfigurationPageInfo.class), is(notNullValue()));
	}

}
