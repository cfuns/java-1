package de.benjaminborbe.filestorage.test;

import de.benjaminborbe.filestorage.api.FilestorageEntry;
import de.benjaminborbe.filestorage.api.FilestorageEntryDto;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Arrays;

public class FilestorageIntegrationTest extends TestCaseOsgi {

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

	public void testFilestorageService() {
		final Object serviceObject = getServiceObject(FilestorageService.class.getName(), null);
		final FilestorageService service = (FilestorageService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.filestorage.service.FilestorageServiceImpl", service.getClass().getName());
	}

	public void testGetFilestorageEntry() throws Exception {
		final byte[] content = "Hello World".getBytes("UTF-8");
		final String contentType = "text/plain";
		final String filename = "readme.txt";

		final FilestorageService filestorageService = getService(FilestorageService.class);
		final FilestorageEntryDto filestorageEntryDto = new FilestorageEntryDto();
		filestorageEntryDto.setContent(content);
		filestorageEntryDto.setContentType(contentType);
		filestorageEntryDto.setFilename(filename);
		final FilestorageEntryIdentifier filestorageEntryIdentifier = filestorageService.createFilestorageEntry(filestorageEntryDto);
		final FilestorageEntry filestorageEntry = filestorageService.getFilestorageEntry(filestorageEntryIdentifier);
		assertEquals(filestorageEntryIdentifier, filestorageEntry.getId());
		assertEquals(contentType, filestorageEntry.getContentType());
		assertEquals(filename, filestorageEntry.getFilename());
		assertTrue(Arrays.equals(content, filestorageEntry.getContent()));
	}

	public void testDeleteFilestorageEntry() throws Exception {
		final FilestorageService filestorageService = getService(FilestorageService.class);
		final FilestorageEntryDto filestorageEntryDto = new FilestorageEntryDto();
		filestorageEntryDto.setContent(new byte[0]);
		filestorageEntryDto.setContentType("text/plain");
		filestorageEntryDto.setFilename("readme.txt");
		final FilestorageEntryIdentifier filestorageEntryIdentifier = filestorageService.createFilestorageEntry(filestorageEntryDto);
		assertNotNull(filestorageEntryIdentifier);

		{
			final FilestorageEntry filestorageEntry = filestorageService.getFilestorageEntry(filestorageEntryIdentifier);
			assertNotNull(filestorageEntry);
		}

		filestorageService.deleteFilestorageEntry(filestorageEntryIdentifier);

		{
			final FilestorageEntry filestorageEntry = filestorageService.getFilestorageEntry(filestorageEntryIdentifier);
			assertNull(filestorageEntry);
		}

	}

}
