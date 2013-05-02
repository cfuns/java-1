package de.benjaminborbe.proxy.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.proxy.core.guice.ProxyModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class ProxyConversationListenerRegistryIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProxyModulesMock());
		assertNotNull(injector.getInstance(ProxyConversationListenerRegistry.class));
	}

}
