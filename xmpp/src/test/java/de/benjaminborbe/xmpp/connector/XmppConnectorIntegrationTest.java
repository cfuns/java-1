package de.benjaminborbe.xmpp.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.xmpp.XmppConstants;
import de.benjaminborbe.xmpp.config.XmppConfig;
import de.benjaminborbe.xmpp.guice.XmppModulesMock;

public class XmppConnectorIntegrationTest {

	private static final int PORT = 5222;

	private static final String HOSTNAME = "127.0.0.1";

	private static boolean xmppNotFound;

	@BeforeClass
	public static void setUp() {
		final Socket socket = new Socket();
		final SocketAddress endpoint = new InetSocketAddress(HOSTNAME, PORT);
		try {
			socket.connect(endpoint, 500);

			xmppNotFound = !socket.isConnected();
			xmppNotFound = false;
		}
		catch (final IOException e) {
			xmppNotFound = true;
		}
	}

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new XmppModulesMock());
		final XmppConnector xmppConnector = injector.getInstance(XmppConnector.class);
		assertNotNull(xmppConnector);
	}

	@Test
	public void testRun() throws Exception {
		if (xmppNotFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new XmppModulesMock());
		final XmppConnector xmppConnector = injector.getInstance(XmppConnector.class);

		final ConfigurationService configurationService = injector.getInstance(ConfigurationService.class);
		configurationService.setConfigurationValue(new ConfigurationIdentifier(XmppConstants.CONFIG_USERNAME), "bb");
		configurationService.setConfigurationValue(new ConfigurationIdentifier(XmppConstants.CONFIG_PASSWORD), "5VCrQO5jMHOE");
		configurationService.setConfigurationValue(new ConfigurationIdentifier(XmppConstants.CONFIG_SERVERHOST), HOSTNAME);
		configurationService.setConfigurationValue(new ConfigurationIdentifier(XmppConstants.CONFIG_SERVERPORT), String.valueOf(PORT));

		final XmppConfig xmppConfig = injector.getInstance(XmppConfig.class);
		assertEquals(HOSTNAME, xmppConfig.getServerHost());
		assertEquals(5222, xmppConfig.getServerPort());
		assertEquals("bb", xmppConfig.getUsername());
		assertEquals("5VCrQO5jMHOE", xmppConfig.getPassword());
		try {
			xmppConnector.connect();

			final List<XmppUser> users = xmppConnector.getUsers();
			assertNotNull(users);

		}
		finally {
			xmppConnector.disconnect();
		}
	}
}
