package de.benjaminborbe.xmpp.connector;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.xmpp.guice.XmppModulesMock;

public class XmppConnectorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new XmppModulesMock());
		final XmppConnector xmppConnector = injector.getInstance(XmppConnector.class);
		assertNotNull(xmppConnector);
	}

	@Ignore
	@Test
	public void testRun() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new XmppModulesMock());
		final XmppConnector xmppConnector = injector.getInstance(XmppConnector.class);
		try {
			xmppConnector.connect();
			final List<XmppUser> users = xmppConnector.getUsers();
			assertNotNull(users);
			assertTrue(users.size() > 0);

			final XmppUser user = xmppConnector.getMe();
			assertNotNull(user);
			xmppConnector.sendMessage(user, "hello");

			Thread.sleep(1000);
		}
		finally {
			xmppConnector.disconnect();
		}
	}
}
