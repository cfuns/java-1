package de.benjaminborbe.xmlrpc.service;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.guice.XmlrpcModulesMock;

public class XmlrpcServiceImplIntegrationTest {

	private final static String CONFLUENCE_URL = "http://confluence.benjamin-borbe.de/rpc/xmlrpc";

	private final static String CONFLUENCE_USERNAME = "test";

	private final static String CONFLUENCE_PASSWORD = "z9W7CUwY4brR";

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new XmlrpcModulesMock());
		assertNotNull(injector.getInstance(XmlrpcService.class));
	}

	@Test
	public void testCall() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new XmlrpcModulesMock());
		final XmlrpcService xmlrpcService = injector.getInstance(XmlrpcService.class);
		final String token = (String) xmlrpcService.execute(new URL(CONFLUENCE_URL), "confluence1.login", new Object[] { CONFLUENCE_USERNAME, CONFLUENCE_PASSWORD });
		assertNotNull(token);
	}
}