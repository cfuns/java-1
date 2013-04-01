package de.benjaminborbe.xmpp.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.xmpp.guice.XmppModulesMock;

public class XmppConfigImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new XmppModulesMock());
		final XmppConfig xmppConfig = injector.getInstance(XmppConfig.class);
		assertNotNull(xmppConfig);
		assertEquals(XmppConfigImpl.class, xmppConfig.getClass());
	}

	@Test
	public void testConfig() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new XmppModulesMock());
		final XmppConfig xmppConfig = injector.getInstance(XmppConfig.class);
		for (final ConfigurationDescription config : xmppConfig.getConfigurations()) {
			assertNotNull(config);
		}
	}
}
