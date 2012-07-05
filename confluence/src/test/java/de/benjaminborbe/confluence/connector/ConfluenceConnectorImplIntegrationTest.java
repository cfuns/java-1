package de.benjaminborbe.confluence.connector;

import static org.junit.Assert.*;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.confluence.guice.ConfluenceModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ConfluenceConnectorImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		assertEquals(ConfluenceConnectorImpl.class, injector.getInstance(ConfluenceConnector.class).getClass());
	}

	@Test
	public void testGetRenderedContent() throws Exception, XmlRpcException {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		final ConfluenceConnector confluenceConnector = injector.getInstance(ConfluenceConnector.class);
		final String confluenceBaseUrl = "http://confluence.benjamin-borbe.de/confluence";
		final String username = "test";
		final String password = "z9W7CUwY4brR";
		final String spaceName = "DEV";
		final String pageName = "Java";
		final String content = confluenceConnector.getRenderedContent(confluenceBaseUrl, username, password, spaceName, pageName);
		assertTrue("didn't find expected string 'Java' in  content!", content.contains("Java"));
	}
}
