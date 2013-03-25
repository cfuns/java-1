package de.benjaminborbe.distributed.search.test;

import java.net.URL;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;

public class DistributedSearchIntegrationTest extends TestCaseOsgi {

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

	public void testDistributedSearchService() {
		final Object serviceObject = getServiceObject(DistributedSearchService.class.getName(), null);
		final DistributedSearchService service = (DistributedSearchService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.distributed.search.service.DistributedSearchServiceImpl", service.getClass().getName());
	}

	public void testSearch() throws Exception {
		final Object serviceObject = getServiceObject(DistributedSearchService.class.getName(), null);
		final DistributedSearchService service = (DistributedSearchService) serviceObject;

		final String index = "defaultIndex";
		final URL url = new URL("http://www.example.com/a");
		final String title = "title";
		final String content = "content";

		service.clear(index);

		assertTrue(service.search(index, "content", 1).isEmpty());

		service.addToIndex(index, url, title, content, null);

		assertFalse(service.search(index, "content", 1).isEmpty());

		service.clear(index);

		assertTrue(service.search(index, "content", 1).isEmpty());
	}
}
