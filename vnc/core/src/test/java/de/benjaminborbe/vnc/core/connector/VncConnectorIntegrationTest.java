package de.benjaminborbe.vnc.core.connector;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.vnc.core.guice.VncModulesMock;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class VncConnectorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncConnector vncConnector = injector.getInstance(VncConnector.class);
		assertNotNull(vncConnector);
	}

}
