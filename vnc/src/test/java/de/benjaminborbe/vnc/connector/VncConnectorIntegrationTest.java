package de.benjaminborbe.vnc.connector;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.vnc.guice.VncModulesMock;

public class VncConnectorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncConnector vncConnector = injector.getInstance(VncConnector.class);
		assertNotNull(vncConnector);
	}

}
