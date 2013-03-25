package de.benjaminborbe.distributed.index.test;

import java.util.Arrays;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;

public class DistributedIndexIntegrationTest extends TestCaseOsgi {

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

	// public void testServices() throws Exception {
	// final BundleContext bundleContext = getContext();
	// assertNotNull(bundleContext);
	// for (final ServiceReference a : bundleContext.getAllServiceReferences(null, null)) {
	// // final Bundle bundle = a.getBundle();
	// final Object service = bundleContext.getService(a);
	// System.err.println(service);
	// }
	// }

	public void testDistributedIndexService() {
		final Object serviceObject = getServiceObject(DistributedIndexService.class.getName(), null);
		final DistributedIndexService service = (DistributedIndexService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.distributed.index.service.DistributedIndexServiceImpl", service.getClass().getName());
	}

	public void testIndex() throws Exception {
		final Object serviceObject = getServiceObject(DistributedIndexService.class.getName(), null);
		final DistributedIndexService service = (DistributedIndexService) serviceObject;
		assertNotNull(service);

		final String index = "testIndex";
		final String id = "pageId";

		final String word = "foobar";
		final Integer rating = 1337;

		service.remove(index, id);
		{
			final DistributedIndexSearchResultIterator iterator = service.search(index, Arrays.asList(word));
			assertNotNull(iterator);
			assertFalse(iterator.hasNext());
		}
		service.add(index, id, new MapChain<String, Integer>().add(word, rating));
		{
			final DistributedIndexSearchResultIterator iterator = service.search(index, Arrays.asList(word));
			assertNotNull(iterator);
			assertTrue(iterator.hasNext());
			final DistributedIndexSearchResult result = iterator.next();
			assertNotNull(result);
			assertEquals(id, result.getId());
			assertEquals(index, result.getIndex());
			assertEquals(rating, result.getRating());
			assertFalse(iterator.hasNext());
		}
	}

}
