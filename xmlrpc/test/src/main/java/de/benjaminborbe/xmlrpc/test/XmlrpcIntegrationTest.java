package de.benjaminborbe.xmlrpc.test;

import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.net.URL;

public class XmlrpcIntegrationTest extends TestCaseOsgi {

	private final static String CONFLUENCE_URL = "https://www.benjamin-borbe.de/confluence/rpc/xmlrpc";

	private final static String CONFLUENCE_USERNAME = "test";

	private final static String CONFLUENCE_PASSWORD = "z9W7CUwY4brR";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetExtHttpService() {
		final BundleContext bundleContext = getContext();
		assertNotNull(bundleContext);
		final ExtHttpServiceMock extHttpService = new ExtHttpServiceMock(new UrlUtilImpl());
		assertNotNull(extHttpService);
		// zum start: keine Dienste registriert
		assertEquals(0, extHttpService.getRegisterFilterCallCounter());
		assertEquals(0, extHttpService.getRegisterServletCallCounter());
		assertEquals(0, extHttpService.getUnregisterFilterCallCounter());
		assertEquals(0, extHttpService.getUnregisterServletCallCounter());
		final ServiceRegistration serviceRegistration = bundleContext.registerService(ExtHttpService.class.getName(), extHttpService, null);
		assertNotNull(serviceRegistration);
		// nach start: Dienste vorhanden?
		assertTrue("no filters registered", extHttpService.getRegisterFilterCallCounter() > 0);
		assertTrue("no servlets registered.", extHttpService.getRegisterServletCallCounter() > 0);
		assertEquals(0, extHttpService.getUnregisterFilterCallCounter());
		assertEquals(0, extHttpService.getUnregisterServletCallCounter());

		// do unregister
		serviceRegistration.unregister();

		assertTrue("no servlets unregistered", extHttpService.getUnregisterServletCallCounter() > 0);
		assertEquals(extHttpService.getRegisterServletCallCounter(), extHttpService.getRegisterServletCallCounter());
		assertEquals(extHttpService.getRegisterFilterCallCounter(), extHttpService.getUnregisterFilterCallCounter());
	}

	public void testXmlrpcService() {
		final Object serviceObject = getServiceObject(XmlrpcService.class.getName(), null);
		final XmlrpcService service = (XmlrpcService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.xmlrpc.service.XmlrpcServiceImpl", service.getClass().getName());
	}

	public void testCall() throws Exception {
		final Object serviceObject = getServiceObject(XmlrpcService.class.getName(), null);
		final XmlrpcService xmlrpcService = (XmlrpcService) serviceObject;
		final String token = (String) xmlrpcService.execute(new URL(CONFLUENCE_URL), "confluence1.login", new Object[]{CONFLUENCE_USERNAME, CONFLUENCE_PASSWORD});
		assertNotNull(token);
	}
}
