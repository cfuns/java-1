package de.benjaminborbe.task.service;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.guice.TaskModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskServiceIntegrationTest {

	private final String username = "username";

	private final String fullname = "fullname";

	private final String password = "password";

	private final String email = "test@test.de";

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
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		// register
		final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(username);
		assertNotNull(userIdentifier);
		assertEquals(username, userIdentifier.getId());
		assertTrue(authenticationService.register(sessionIdentifier, userIdentifier, email, password, fullname));

		// login
		assertTrue(authenticationService.login(sessionIdentifier, userIdentifier, password));
		assertEquals(userIdentifier, authenticationService.getCurrentUser(sessionIdentifier));

		assertEquals(0, taskService.getTasksNotCompleted(sessionIdentifier).size());

		final String name = "nameA";
		final String description = "descriptionA";
		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, name, description, null, null, null, null, null, null, null);
		assertNotNull(taskIdentifier);

		assertEquals(1, taskService.getTasksNotCompleted(sessionIdentifier).size());

		taskService.deleteTask(sessionIdentifier, taskIdentifier);

		assertEquals(0, taskService.getTasksNotCompleted(sessionIdentifier).size());
	}

	@Test
	public void testDeleteTask() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);

		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		// register
		final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(username);
		assertNotNull(userIdentifier);
		assertEquals(username, userIdentifier.getId());
		assertTrue(authenticationService.register(sessionIdentifier, userIdentifier, email, password, fullname));

		// login
		assertTrue(authenticationService.login(sessionIdentifier, userIdentifier, password));
		assertEquals(userIdentifier, authenticationService.getCurrentUser(sessionIdentifier));

		assertEquals(0, taskService.getTasksNotCompleted(sessionIdentifier).size());

		final TaskIdentifier taskIdentifierA = taskService.createTask(sessionIdentifier, "nameA", "descriptionA", null, null, null, null, null, null, null);
		assertNotNull(taskIdentifierA);
		final TaskIdentifier taskIdentifierB = taskService.createTask(sessionIdentifier, "nameA", "descriptionA", null, taskIdentifierA, null, null, null, null, null);
		assertNotNull(taskIdentifierB);
		final TaskIdentifier taskIdentifierC = taskService.createTask(sessionIdentifier, "nameA", "descriptionA", null, taskIdentifierB, null, null, null, null, null);
		assertNotNull(taskIdentifierC);

		assertEquals(3, taskService.getTasksNotCompleted(sessionIdentifier).size());

		{
			final Task taskA = taskService.getTask(sessionIdentifier, taskIdentifierA);
			assertNull(taskA.getParentId());
			final Task taskB = taskService.getTask(sessionIdentifier, taskIdentifierB);
			assertEquals(taskA.getId(), taskB.getParentId());
			final Task taskC = taskService.getTask(sessionIdentifier, taskIdentifierC);
			assertEquals(taskB.getId(), taskC.getParentId());
		}

		taskService.deleteTask(sessionIdentifier, taskIdentifierB);
		assertEquals(2, taskService.getTasksNotCompleted(sessionIdentifier).size());

		{
			final Task taskA = taskService.getTask(sessionIdentifier, taskIdentifierA);
			assertNull(taskA.getParentId());
			final Task taskB = taskService.getTask(sessionIdentifier, taskIdentifierB);
			assertNull(taskB);
			final Task taskC = taskService.getTask(sessionIdentifier, taskIdentifierC);
			assertEquals(taskA.getId(), taskC.getParentId());
		}

		taskService.deleteTask(sessionIdentifier, taskIdentifierA);
		assertEquals(1, taskService.getTasksNotCompleted(sessionIdentifier).size());

		{
			final Task taskA = taskService.getTask(sessionIdentifier, taskIdentifierA);
			assertNull(taskA);
			final Task taskB = taskService.getTask(sessionIdentifier, taskIdentifierB);
			assertNull(taskB);
			final Task taskC = taskService.getTask(sessionIdentifier, taskIdentifierC);
			assertNull(taskC.getParentId());
		}
	}
}
