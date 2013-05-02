package de.benjaminborbe.httpdownloader.test;

import de.benjaminborbe.httpdownloader.api.HttpRequestDto;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.net.URL;

public class HttpdownloaderIntegrationTest extends TestCaseOsgi {

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

	public void testHttpdownloaderService() throws Exception {
		final Object serviceObject = getServiceObject(HttpdownloaderService.class.getName(), null);
		final HttpdownloaderService service = (HttpdownloaderService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.httpdownloader.core.service.HttpdownloaderCoreServiceImpl", service.getClass().getName());
	}

	public void testGet() throws Exception {
		final HttpdownloaderService httpdownloaderService = getService(HttpdownloaderService.class);
		final HttpRequestDto httpRequest = new HttpRequestDto();
		final URL url = new URL("http://www.google.de");
		httpRequest.setUrl(url);
		final HttpResponse httpResponse = httpdownloaderService.getSecure(httpRequest);
		assertNotNull(httpResponse);
		assertNotNull(httpResponse.getContent());
		assertNotNull(httpResponse.getDuration());
		assertNotNull(httpResponse.getContent());
		assertNotNull(httpResponse.getUrl());
		assertEquals(url, httpResponse.getUrl());
	}

}
