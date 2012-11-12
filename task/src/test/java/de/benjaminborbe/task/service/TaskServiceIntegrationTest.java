package de.benjaminborbe.task.service;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.guice.TaskModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskServiceIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final TaskService taskService = injector.getInstance(TaskService.class);
		assertNotNull(taskService);
		assertEquals(TaskServiceImpl.class, taskService.getClass());
	}

	@Test
	public void testCreateListDelete() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		final TaskService taskService = injector.getInstance(TaskService.class);
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);

		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
		final String username = "username";
		final String fullname = "fullname";
		final String password = "password";
		final String email = "test@test.de";
		final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(username);
		assertNotNull(userIdentifier);
		assertTrue(authenticationService.register(sessionIdentifier, userIdentifier, email, password, fullname));

		assertTrue(authenticationService.login(sessionIdentifier, userIdentifier, password));
		assertEquals(userIdentifier, authenticationService.getCurrentUser(sessionIdentifier));

		assertEquals(0, taskService.getTasksNotCompleted(sessionIdentifier, 1).size());

		final String name = "nameA";
		final String description = "descriptionA";
		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, name, description, null, null, null, null, null);
		assertNotNull(taskIdentifier);

		assertEquals(1, taskService.getTasksNotCompleted(sessionIdentifier, 1).size());

		taskService.deleteTask(sessionIdentifier, taskIdentifier);

		assertEquals(0, taskService.getTasksNotCompleted(sessionIdentifier, 1).size());
	}
}
