package de.benjaminborbe.sample.test;

import de.benjaminborbe.sample.api.SampleService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class SampleIntegrationTest extends TestCaseOsgi {

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

	public void testSampleService() throws Exception {
		final Object serviceObject = getServiceObject(SampleService.class.getName(), null);
		final SampleService service = (SampleService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.sample.core.service.SampleCoreServiceImpl", service.getClass().getName());
	}


	public void testCalc() throws Exception {
		final SampleService sampleService = getService(SampleService.class);
		assertEquals(46, sampleService.calc(23));
	}

}
