package de.benjaminborbe.task.test;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.task.api.TaskAttachmentIdentifier;
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.test.osgi.TestUtil;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Collection;

public class TaskIntegrationTest extends TestCaseOsgi {

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

	public void testTaskService() {
		final Object serviceObject = getServiceObject(TaskService.class.getName(), null);
		final TaskService service = (TaskService) serviceObject;
		assertNotNull(service);
		assertEquals("de.benjaminborbe.task.core.service.TaskServiceImpl", service.getClass().getName());
	}

	public void testCreateTask() throws Exception {
		final AuthenticationService authenticationService = getService(AuthenticationService.class);
		final StorageService storageService = getService(StorageService.class);
		final TaskService taskService = getService(TaskService.class);
		final TestUtil testUtil = new TestUtil(authenticationService, storageService);
		final SessionIdentifier sessionIdentifier = testUtil.createSessionIdentifier();
		testUtil.createSuperAdmin(sessionIdentifier);
		final TaskDto task = new TaskDto();
		task.setName("TestTask");
		task.setFocus(TaskFocus.INBOX);
		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, task);
		assertNotNull(taskIdentifier);
	}

	public void testGetAttachments() throws Exception {
		final AuthenticationService authenticationService = getService(AuthenticationService.class);
		final StorageService storageService = getService(StorageService.class);
		final TaskService taskService = getService(TaskService.class);
		final TestUtil testUtil = new TestUtil(authenticationService, storageService);
		final SessionIdentifier sessionIdentifier = testUtil.createSessionIdentifier();
		testUtil.createSuperAdmin(sessionIdentifier);
		final TaskDto task = new TaskDto();
		task.setName("TestTask");
		task.setFocus(TaskFocus.INBOX);
		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, task);
		final Collection<TaskAttachmentIdentifier> attachments = taskService.getAttachmentIdentifiers(sessionIdentifier, taskIdentifier);
		assertNotNull(attachments);
		assertEquals(0, attachments.size());
	}

}
