package de.benjaminborbe.task.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.guice.TaskModulesMock;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskServiceImplIntegrationTest {

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

	private SessionIdentifier getLoginSession(final AuthenticationService authenticationService) throws AuthenticationServiceException, ValidationException {
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		// register
		final UserIdentifier userIdentifier = authenticationService.register(sessionIdentifier, username, email, password, fullname, TimeZone.getDefault());
		assertEquals(username, userIdentifier.getId());
		// login
		assertTrue(authenticationService.login(sessionIdentifier, userIdentifier, password));
		assertEquals(userIdentifier, authenticationService.getCurrentUser(sessionIdentifier));
		return sessionIdentifier;
	}

	@Test
	public void testCreateListDelete() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService);

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

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService);

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

	@Test
	public void testComplete() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService);

		assertEquals(0, taskService.getTasksNotCompleted(sessionIdentifier).size());

		final TaskIdentifier taskIdentifierA = taskService.createTask(sessionIdentifier, "nameA", "descriptionA", null, null, null, null, null, null, null);
		assertNotNull(taskIdentifierA);
		final TaskIdentifier taskIdentifierB = taskService.createTask(sessionIdentifier, "nameA", "descriptionA", null, taskIdentifierA, null, null, null, null, null);
		assertNotNull(taskIdentifierB);

		assertEquals(2, taskService.getTasksNotCompleted(sessionIdentifier).size());

		try {
			taskService.completeTask(sessionIdentifier, taskIdentifierA);
			fail("ValidationException expected");
		}
		catch (final ValidationException e) {
			assertNotNull(e);
		}

		assertEquals(2, taskService.getTasksNotCompleted(sessionIdentifier).size());

		taskService.completeTask(sessionIdentifier, taskIdentifierB);

		assertEquals(1, taskService.getTasksNotCompleted(sessionIdentifier).size());

		taskService.completeTask(sessionIdentifier, taskIdentifierA);

		assertEquals(0, taskService.getTasksNotCompleted(sessionIdentifier).size());

	}

	@Test
	public void testCreateChildStartDue() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);
		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService);

		// both null
		{
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertNull(child.getStart());
			assertNull(child.getDue());
		}

		// parent null, child not
		{
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, start, due, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child null
		{
			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, start, due, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(start, parent.getStart());
			assertEquals(due, parent.getDue());

			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("2d");
			final Calendar parentDue = calendarUtil.parseSmart("3d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, parentStart, parentDue, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(parentStart, parent.getStart());
			assertEquals(parentDue, parent.getDue());

			final Calendar childStart = calendarUtil.parseSmart("1d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, childStart, childDue, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(parentStart, child.getStart());
			assertEquals(parentDue, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("1d");
			final Calendar parentDue = calendarUtil.parseSmart("5d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, parentStart, parentDue, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(parentStart, parent.getStart());
			assertEquals(parentDue, parent.getDue());

			final Calendar childStart = calendarUtil.parseSmart("2d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, childStart, childDue, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(childStart, child.getStart());
			assertEquals(childDue, child.getDue());
		}
	}

	@Test
	public void testUpdateChildStartDue() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);
		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService);

		// both null
		{
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			taskService.updateTask(sessionIdentifier, childTaskIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertNull(child.getStart());
			assertNull(child.getDue());
		}

		// parent null, child not
		{
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			taskService.updateTask(sessionIdentifier, childTaskIdentifier, "child", null, null, parentTaskIdentifier, start, due, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child null
		{
			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, start, due, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(start, parent.getStart());
			assertEquals(due, parent.getDue());

			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			taskService.updateTask(sessionIdentifier, childTaskIdentifier, "child", null, null, parentTaskIdentifier, start, due, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("2d");
			final Calendar parentDue = calendarUtil.parseSmart("3d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, parentStart, parentDue, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(parentStart, parent.getStart());
			assertEquals(parentDue, parent.getDue());

			final Calendar childStart = calendarUtil.parseSmart("1d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			taskService.updateTask(sessionIdentifier, childTaskIdentifier, "child", null, null, parentTaskIdentifier, childStart, childDue, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(parentStart, child.getStart());
			assertEquals(parentDue, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("1d");
			final Calendar parentDue = calendarUtil.parseSmart("4d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, parentStart, parentDue, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(parentStart, parent.getStart());
			assertEquals(parentDue, parent.getDue());

			final Calendar childStart = calendarUtil.parseSmart("2d");
			final Calendar childDue = calendarUtil.parseSmart("3d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			taskService.updateTask(sessionIdentifier, childTaskIdentifier, "child", null, null, parentTaskIdentifier, childStart, childDue, null, null, null);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(childStart, child.getStart());
			assertEquals(childDue, child.getDue());
		}
	}

	@Test
	public void testUpdateParentStartDue() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);
		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService);

		// both null
		{
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			assertNotNull(childTaskIdentifier);

			taskService.updateTask(sessionIdentifier, parentTaskIdentifier, "child", null, null, null, null, null, null, null, null);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertNull(child.getStart());
			assertNull(child.getDue());
		}

		// parent null, child not
		{
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, start, due, null, null, null);
			assertNotNull(childTaskIdentifier);

			taskService.updateTask(sessionIdentifier, parentTaskIdentifier, "child", null, null, null, null, null, null, null, null);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child null
		{
			final Calendar parentStart = calendarUtil.parseSmart("1d");
			final Calendar parentDue = calendarUtil.parseSmart("2d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);

			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, null, null, null, null, null);
			assertNotNull(childTaskIdentifier);

			taskService.updateTask(sessionIdentifier, parentTaskIdentifier, "child", null, null, null, parentStart, parentDue, null, null, null);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(parentStart, child.getStart());
			assertEquals(parentDue, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("2d");
			final Calendar parentDue = calendarUtil.parseSmart("3d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);

			final Calendar childStart = calendarUtil.parseSmart("1d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, childStart, childDue, null, null, null);
			assertNotNull(childTaskIdentifier);

			taskService.updateTask(sessionIdentifier, parentTaskIdentifier, "child", null, null, null, parentStart, parentDue, null, null, null);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(parentStart, child.getStart());
			assertEquals(parentDue, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("1d");
			final Calendar parentDue = calendarUtil.parseSmart("5d");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, "parent", null, null, null, null, null, null, null, null);
			assertNotNull(parentTaskIdentifier);

			final Calendar childStart = calendarUtil.parseSmart("3d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, "child", null, null, parentTaskIdentifier, childStart, childDue, null, null, null);
			assertNotNull(childTaskIdentifier);

			taskService.updateTask(sessionIdentifier, parentTaskIdentifier, "child", null, null, null, parentStart, parentDue, null, null, null);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(childStart, child.getStart());
			assertEquals(childDue, child.getDue());
		}
	}

}
