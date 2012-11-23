package de.benjaminborbe.confluence.connector;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.confluence.ConfluenceTestConstants;
import de.benjaminborbe.confluence.guice.ConfluenceModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ConfluenceConnectorImplIntegrationTest {

	private static boolean notFound = true;

	@BeforeClass
	public static void setUp() {
		final Socket socket = new Socket();
		final SocketAddress endpoint = new InetSocketAddress(ConfluenceTestConstants.CONFLUENCE_URL, 80);
		try {
			socket.connect(endpoint, 500);
			notFound = !socket.isConnected();
			notFound = false;
		}
		catch (final IOException e) {
			notFound = true;
		}
	}

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		assertEquals(ConfluenceConnectorImpl.class, injector.getInstance(ConfluenceConnector.class).getClass());
	}

	@Test
	public void testGetRenderedContent() throws Exception, XmlRpcException {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		final ConfluenceConnector confluenceConnector = injector.getInstance(ConfluenceConnector.class);
		final String spaceName = "DEV";
		final String pageName = "Java";
		final String content = confluenceConnector.getRenderedContent(ConfluenceTestConstants.CONFLUENCE_URL, ConfluenceTestConstants.CONFLUENCE_USERNAME,
				ConfluenceTestConstants.CONFLUENCE_PASSWORD, spaceName, pageName);
		assertTrue("didn't find expected string 'Java' in  content!", content.contains("Java"));
	}
}
