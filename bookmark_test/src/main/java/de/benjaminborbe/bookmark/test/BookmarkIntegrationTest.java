package de.benjaminborbe.bookmark.test;

import java.util.Arrays;
import java.util.List;

import org.apache.felix.http.api.ExtHttpService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;

public class BookmarkIntegrationTest extends TestCaseOsgi {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
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

	@Test
	public void testBookmarkService() {
		final Object serviceObject = getServiceObject(BookmarkService.class.getName(), null);
		final BookmarkService service = (BookmarkService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.bookmark.service.BookmarkServiceImpl", service.getClass().getName());
	}

	@Test
	public void testSearchServiceComponent() {
		final Object[] serviceObjects = getServiceObjects(SearchServiceComponent.class.getName(), null);
		final List<String> list = Arrays.asList("de.benjaminborbe.bookmark.service.BookmarkSearchServiceComponent",
				"de.benjaminborbe.bookmark.service.RegisteredServletSearchServiceComponent");
		assertTrue(serviceObjects.length >= list.size());
		for (final String name : list) {
			boolean match = false;
			for (final Object serviceObject : serviceObjects) {
				final SearchServiceComponent service = (SearchServiceComponent) serviceObject;
				assertNotNull(service);
				if (name.equals(service.getClass().getName())) {
					match = true;
				}
			}
			assertTrue(match);
		}
	}

	@Test
	public void testDashboardContentWidget() {
		final Object serviceObject = getServiceObject(DashboardContentWidget.class.getName(), null);
		final DashboardContentWidget service = (DashboardContentWidget) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.bookmark.gui.service.BookmarkGuiFavoriteDashboardWidget", service.getClass().getName());
	}
}
