package de.benjaminborbe.storage.test;

import org.apache.felix.http.api.ExtHttpService;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.storage.api.CacheStorageService;
import de.benjaminborbe.storage.api.PersistentStorageService;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

public class StorageTest extends OSGiTestCase {

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
		final ExtHttpServiceMock extHttpService = new ExtHttpServiceMock();
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

	protected StorageService getCacheStorageService() {
		final Object serviceObject = getServiceObject(CacheStorageService.class.getName(), null);
		final CacheStorageService storageService = (CacheStorageService) serviceObject;
		return storageService;
	}

	protected StorageService getPersistentStorageService() {
		final Object serviceObject = getServiceObject(PersistentStorageService.class.getName(), null);
		final PersistentStorageService storageService = (PersistentStorageService) serviceObject;
		return storageService;
	}

	public void testGetPersistentStorageService() {
		final StorageService storageService = getPersistentStorageService();
		assertNotNull(storageService);
		assertEquals("de.benjaminborbe.storage.service.PersistentStorageServiceImpl", storageService.getClass().getName());
	}

	public void testGetCacheStorageService() {
		final StorageService storageService = getCacheStorageService();
		assertNotNull(storageService);
		assertEquals("de.benjaminborbe.storage.service.CacheStorageServiceImpl", storageService.getClass().getName());
	}

}
