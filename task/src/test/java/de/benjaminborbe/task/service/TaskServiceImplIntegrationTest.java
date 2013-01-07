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
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.guice.TaskModulesMock;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskServiceImplIntegrationTest {

	private final String username = "username";

	private final String fullname = "fullname";

	private final String password = "password";

	private final String email = "test@example.com";

	private final String validateEmailBaseUrl = "http://example.com/test";

	private final String shortenUrl = "http://bb.de/s";

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
		final UserIdentifier userIdentifier = authenticationService.register(sessionIdentifier, shortenUrl, validateEmailBaseUrl, username, email, password, fullname,
				TimeZone.getDefault());
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
		final TaskDto taskDto = new TaskDto();
		taskDto.setName(name);
		taskDto.setDescription(description);

		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, taskDto);
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

		final TaskDto taskDtoA = new TaskDto();
		taskDtoA.setName("nameA");
		taskDtoA.setDescription("descriptionA");
		final TaskIdentifier taskIdentifierA = taskService.createTask(sessionIdentifier, taskDtoA);
		assertNotNull(taskIdentifierA);

		final TaskDto taskDtoB = new TaskDto();
		taskDtoB.setName("nameA");
		taskDtoB.setDescription("descriptionA");
		taskDtoB.setParentId(taskIdentifierA);
		final TaskIdentifier taskIdentifierB = taskService.createTask(sessionIdentifier, taskDtoB);
		assertNotNull(taskIdentifierB);

		final TaskDto taskDtoC = new TaskDto();
		taskDtoC.setName("nameA");
		taskDtoC.setDescription("descriptionA");
		taskDtoC.setParentId(taskIdentifierB);
		final TaskIdentifier taskIdentifierC = taskService.createTask(sessionIdentifier, taskDtoC);
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

		final TaskDto taskDtoA = new TaskDto();
		taskDtoA.setName("nameA");
		taskDtoA.setDescription("descriptionA");

		final TaskIdentifier taskIdentifierA = taskService.createTask(sessionIdentifier, taskDtoA);
		assertNotNull(taskIdentifierA);

		final TaskDto taskDtoB = new TaskDto();
		taskDtoB.setName("nameA");
		taskDtoB.setDescription("descriptionA");
		taskDtoB.setParentId(taskIdentifierA);

		final TaskIdentifier taskIdentifierB = taskService.createTask(sessionIdentifier, taskDtoB);
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

			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertNull(child.getStart());
			assertNull(child.getDue());
		}

		// parent null, child not
		{
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			taskDtoB.setStart(start);
			taskDtoB.setDue(due);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child null
		{
			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setStart(start);
			taskDtoA.setDue(due);
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(start, parent.getStart());
			assertEquals(due, parent.getDue());

			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("2d");
			final Calendar parentDue = calendarUtil.parseSmart("3d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setStart(parentStart);
			taskDtoA.setDue(parentDue);
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(parentStart, parent.getStart());
			assertEquals(parentDue, parent.getDue());

			final Calendar childStart = calendarUtil.parseSmart("1d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			taskDtoB.setStart(childStart);
			taskDtoB.setDue(childDue);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(parentStart, child.getStart());
			assertEquals(parentDue, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("1d");
			final Calendar parentDue = calendarUtil.parseSmart("5d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setStart(parentStart);
			taskDtoA.setDue(parentDue);
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(parentStart, parent.getStart());
			assertEquals(parentDue, parent.getDue());

			final Calendar childStart = calendarUtil.parseSmart("2d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			taskDtoB.setStart(childStart);
			taskDtoB.setDue(childDue);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
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
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(childTaskIdentifier);
			taskDtoC.setName("child");
			taskDtoC.setParentId(parentTaskIdentifier);
			taskService.updateTask(sessionIdentifier, taskDtoC);

			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertNull(child.getStart());
			assertNull(child.getDue());
		}

		// parent null, child not
		{
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(childTaskIdentifier);
			taskDtoC.setName("child");
			taskDtoC.setParentId(parentTaskIdentifier);
			taskDtoC.setStart(start);
			taskDtoC.setDue(due);
			taskService.updateTask(sessionIdentifier, taskDtoC);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child null
		{
			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setStart(start);
			taskDtoA.setDue(due);
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(start, parent.getStart());
			assertEquals(due, parent.getDue());

			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(childTaskIdentifier);
			taskDtoC.setName("child");
			taskDtoC.setParentId(parentTaskIdentifier);
			taskDtoC.setStart(start);
			taskDtoC.setDue(due);
			taskService.updateTask(sessionIdentifier, taskDtoC);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("2d");
			final Calendar parentDue = calendarUtil.parseSmart("3d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setStart(parentStart);
			taskDtoA.setDue(parentDue);
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(parentStart, parent.getStart());
			assertEquals(parentDue, parent.getDue());

			final Calendar childStart = calendarUtil.parseSmart("1d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(childTaskIdentifier);
			taskDtoC.setName("child");
			taskDtoC.setParentId(parentTaskIdentifier);
			taskDtoC.setStart(childStart);
			taskDtoC.setDue(childDue);
			taskService.updateTask(sessionIdentifier, taskDtoC);
			assertNotNull(childTaskIdentifier);
			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(parentStart, child.getStart());
			assertEquals(parentDue, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("1d");
			final Calendar parentDue = calendarUtil.parseSmart("4d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setStart(parentStart);
			taskDtoA.setDue(parentDue);
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertEquals(parentStart, parent.getStart());
			assertEquals(parentDue, parent.getDue());

			final Calendar childStart = calendarUtil.parseSmart("2d");
			final Calendar childDue = calendarUtil.parseSmart("3d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(childTaskIdentifier);
			taskDtoC.setName("child");
			taskDtoC.setParentId(parentTaskIdentifier);
			taskDtoC.setStart(childStart);
			taskDtoC.setDue(childDue);
			taskService.updateTask(sessionIdentifier, taskDtoC);
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
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(parentTaskIdentifier);
			taskDtoC.setName("parent");
			taskService.updateTask(sessionIdentifier, taskDtoC);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertNull(child.getStart());
			assertNull(child.getDue());
		}

		// parent null, child not
		{
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);
			final Task parent = taskService.getTask(sessionIdentifier, parentTaskIdentifier);
			assertNull(parent.getStart());
			assertNull(parent.getDue());

			final Calendar start = calendarUtil.parseSmart("1d");
			final Calendar due = calendarUtil.parseSmart("2d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			taskDtoB.setStart(start);
			taskDtoB.setDue(due);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(parentTaskIdentifier);
			taskDtoC.setName("parent");
			taskService.updateTask(sessionIdentifier, taskDtoC);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(start, child.getStart());
			assertEquals(due, child.getDue());
		}

		// child null
		{
			final Calendar parentStart = calendarUtil.parseSmart("1d");
			final Calendar parentDue = calendarUtil.parseSmart("2d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);

			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(parentTaskIdentifier);
			taskDtoC.setName("child");
			taskDtoC.setStart(parentStart);
			taskDtoC.setDue(parentDue);
			taskService.updateTask(sessionIdentifier, taskDtoC);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(parentStart, child.getStart());
			assertEquals(parentDue, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("2d");
			final Calendar parentDue = calendarUtil.parseSmart("3d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);

			final Calendar childStart = calendarUtil.parseSmart("1d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			taskDtoB.setStart(childStart);
			taskDtoB.setDue(childDue);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(parentTaskIdentifier);
			taskDtoC.setName("child");
			taskDtoC.setStart(parentStart);
			taskDtoC.setDue(parentDue);
			taskService.updateTask(sessionIdentifier, taskDtoC);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(parentStart, child.getStart());
			assertEquals(parentDue, child.getDue());
		}

		// child change
		{
			final Calendar parentStart = calendarUtil.parseSmart("1d");
			final Calendar parentDue = calendarUtil.parseSmart("5d");
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			final TaskIdentifier parentTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoA);
			assertNotNull(parentTaskIdentifier);

			final Calendar childStart = calendarUtil.parseSmart("3d");
			final Calendar childDue = calendarUtil.parseSmart("4d");
			final TaskDto taskDtoB = new TaskDto();
			taskDtoB.setName("child");
			taskDtoB.setParentId(parentTaskIdentifier);
			taskDtoB.setStart(childStart);
			taskDtoB.setDue(childDue);
			final TaskIdentifier childTaskIdentifier = taskService.createTask(sessionIdentifier, taskDtoB);
			assertNotNull(childTaskIdentifier);

			final TaskDto taskDtoC = new TaskDto();
			taskDtoC.setId(parentTaskIdentifier);
			taskDtoC.setName("child");
			taskDtoC.setStart(parentStart);
			taskDtoC.setDue(parentDue);
			taskService.updateTask(sessionIdentifier, taskDtoC);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertEquals(childStart, child.getStart());
			assertEquals(childDue, child.getDue());
		}
	}

	@Test
	public void testCreateTaskFocus() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService);

		{
			final TaskDto taskDto = new TaskDto();
			taskDto.setName("task");
			taskDto.setFocus(null);

			final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, taskDto);
			assertNotNull(taskIdentifier);

			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			assertEquals(TaskFocus.INBOX, task.getFocus());
		}
		{
			final TaskDto taskDto = new TaskDto();
			taskDto.setName("task");
			taskDto.setFocus(TaskFocus.SOMEDAY);

			final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, taskDto);
			assertNotNull(taskIdentifier);

			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			assertEquals(TaskFocus.SOMEDAY, task.getFocus());
		}
	}

	@Test
	public void testUpdateTaskFocus() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService);

		final TaskDto taskDtoOrg = new TaskDto();
		taskDtoOrg.setName("task");

		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, taskDtoOrg);
		assertNotNull(taskIdentifier);

		{
			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			assertEquals(TaskFocus.INBOX, task.getFocus());
		}

		{
			final TaskDto taskDto = new TaskDto();
			taskDto.setId(taskIdentifier);
			taskDto.setName("task");
			taskDto.setFocus(TaskFocus.SOMEDAY);
			taskService.updateTask(sessionIdentifier, taskDto);
		}

		{
			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			assertEquals(TaskFocus.SOMEDAY, task.getFocus());
		}

		{
			final TaskDto taskDto = new TaskDto();
			taskDto.setId(taskIdentifier);
			taskDto.setName("task");
			taskDto.setFocus(null);
			taskService.updateTask(sessionIdentifier, taskDto);
		}

		{
			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			assertEquals(TaskFocus.INBOX, task.getFocus());
		}

	}

}
