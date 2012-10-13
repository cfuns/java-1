package de.benjaminborbe.vnc.connector;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.glavsoft.rfb.client.ClientToServerMessage;
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

	@Ignore
	@Test
	public void testname() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncConnector vncConnector = injector.getInstance(VncConnector.class);
		vncConnector.connect();
		final Key key = null;
		vncConnector.keyPress(key);
		vncConnector.keyRelease(key);

		Thread.sleep(10000);

		for (final ClientToServerMessage a : vncConnector.getHistory().getList()) {
			System.err.println(a);
		}
		// [KeyEventMessage: [down-flag: true, key: 97(61)]
		// [KeyEventMessage: [down-flag: false, key: 97(61)]

		// PointerEventMessage: [x: 525, y: 201, button-mask: 0]
		// PointerEventMessage: [x: 522, y: 199, button-mask: 0]
		// PointerEventMessage: [x: 520, y: 197, button-mask: 0]
		// PointerEventMessage: [x: 518, y: 196, button-mask: 0]
		// PointerEventMessage: [x: 517, y: 195, button-mask: 0]
		// PointerEventMessage: [x: 517, y: 195, button-mask: 1]
	}
}
