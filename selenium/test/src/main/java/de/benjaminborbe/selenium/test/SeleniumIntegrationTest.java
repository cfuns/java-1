package de.benjaminborbe.selenium.test;

import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class SeleniumIntegrationTest extends TestCaseOsgi {

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

	public void testSeleniumService() throws Exception {
		final Object serviceObject = getServiceObject(SeleniumService.class.getName(), null);
		final SeleniumService service = (SeleniumService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.selenium.core.service.SeleniumCoreServiceImpl", service.getClass().getName());
	}

	public void testSeleniumConfigurationXmlService() throws Exception {
		final Object serviceObject = getServiceObject(SeleniumConfigurationXmlService.class.getName(), null);
		final SeleniumConfigurationXmlService service = (SeleniumConfigurationXmlService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.selenium.configuration.xml.service.SeleniumConfigurationXmlServiceImpl", service.getClass().getName());
	}

	public void testCreateSeleniumConfigurationIdentifier() throws Exception {
		final SeleniumService seleniumService = getService(SeleniumService.class);

		assertNull(seleniumService.createSeleniumConfigurationIdentifier(null));
		assertNull(seleniumService.createSeleniumConfigurationIdentifier(""));
		assertNull(seleniumService.createSeleniumConfigurationIdentifier(" "));

		assertNotNull(seleniumService.createSeleniumConfigurationIdentifier("1337"));
		assertEquals("1337", seleniumService.createSeleniumConfigurationIdentifier("1337").getId());

		assertNotNull(seleniumService.createSeleniumConfigurationIdentifier(" 1337 "));
		assertEquals("1337", seleniumService.createSeleniumConfigurationIdentifier(" 1337 ").getId());

	}

}
