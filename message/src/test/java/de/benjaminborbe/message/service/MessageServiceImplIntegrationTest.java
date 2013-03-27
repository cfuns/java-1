package de.benjaminborbe.message.service;

import com.google.inject.Injector;
import de.benjaminborbe.message.MessageActivator;
import de.benjaminborbe.message.guice.MessageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MessageServiceImplIntegrationTest {


	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MessageModulesMock());
		assertNotNull(injector.getInstance(MessageActivator.class));
	}

}
