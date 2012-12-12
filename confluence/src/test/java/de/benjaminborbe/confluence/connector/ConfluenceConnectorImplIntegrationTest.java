package de.benjaminborbe.confluence.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collection;

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
	public void testGetRenderedContent() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		final ConfluenceConnector confluenceConnector = injector.getInstance(ConfluenceConnector.class);
		final String spaceName = "DEV";
		final String pageName = "Java";
		final String token = confluenceConnector
				.login(ConfluenceTestConstants.CONFLUENCE_URL, ConfluenceTestConstants.CONFLUENCE_USERNAME, ConfluenceTestConstants.CONFLUENCE_PASSWORD);
		final String content = confluenceConnector.getRenderedContent(ConfluenceTestConstants.CONFLUENCE_URL, token, spaceName, pageName);
		assertTrue("didn't find expected string 'Java' in  content!", content.contains("Java"));
	}

	@Test
	public void testGetSpaces() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		final ConfluenceConnector confluenceConnector = injector.getInstance(ConfluenceConnector.class);
		final String token = confluenceConnector
				.login(ConfluenceTestConstants.CONFLUENCE_URL, ConfluenceTestConstants.CONFLUENCE_USERNAME, ConfluenceTestConstants.CONFLUENCE_PASSWORD);
		final Collection<String> spaces = confluenceConnector.getSpaceKeys(ConfluenceTestConstants.CONFLUENCE_URL, token);
		assertNotNull(spaces);
		assertTrue(spaces.size() > 0);
	}

	@Test
	public void testGetPages() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		final ConfluenceConnector confluenceConnector = injector.getInstance(ConfluenceConnector.class);
		final String token = confluenceConnector
				.login(ConfluenceTestConstants.CONFLUENCE_URL, ConfluenceTestConstants.CONFLUENCE_USERNAME, ConfluenceTestConstants.CONFLUENCE_PASSWORD);
		final String spaceName = "DEV";
		final Collection<ConfluenceConnectorPage> pages = confluenceConnector.getPages(ConfluenceTestConstants.CONFLUENCE_URL, token, spaceName);
		assertNotNull(pages);
		assertTrue(pages.size() > 0);
		assertNotNull(confluenceConnector.getRenderedContent(ConfluenceTestConstants.CONFLUENCE_URL, token, pages.iterator().next().getPageId()));
	}
}
