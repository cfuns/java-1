package de.benjaminborbe.task.service;

import com.google.inject.Injector;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskAttachmentDto;
import de.benjaminborbe.task.api.TaskAttachmentIdentifier;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.guice.TaskModulesMock;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TaskServiceImplIntegrationTest {

	private static final String USERNAME = "USERNAME";

	private static final String FULLNAME = "FULLNAME";

	private static final String PASSWORD = "PASSWORD";

	private static final String EMAIL = "test@example.com";

	private static final String VALIDATE_EMAIL_BASE_URL = "http://example.com/test";

	private static final String SHORTEN_URL = "http://bb.de/s";

	private static final String TASK_NAME = "testName";

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final TaskService taskService = injector.getInstance(TaskService.class);
		assertNotNull(taskService);
		assertEquals(TaskServiceImpl.class, taskService.getClass());
	}

	private SessionIdentifier getLoginSession(final AuthenticationService authenticationService, final String username) throws AuthenticationServiceException, ValidationException {
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

		// register
		final UserIdentifier userIdentifier = authenticationService.register(sessionIdentifier, SHORTEN_URL, VALIDATE_EMAIL_BASE_URL, username, EMAIL, PASSWORD, FULLNAME,
			TimeZone.getDefault());
		assertEquals(username, userIdentifier.getId());
		// login
		assertTrue(authenticationService.login(sessionIdentifier, userIdentifier, PASSWORD));
		assertEquals(userIdentifier, authenticationService.getCurrentUser(sessionIdentifier));
		return sessionIdentifier;
	}

	@Test
	public void testCreateListDelete() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		assertEquals(0, taskService.getTasks(sessionIdentifier, false).size());

		final String name = "nameA";
		final String description = "descriptionA";
		final TaskDto taskDto = new TaskDto();
		taskDto.setName(name);
		taskDto.setDescription(description);
		taskDto.setFocus(TaskFocus.INBOX);

		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, taskDto);
		assertNotNull(taskIdentifier);

		assertEquals(1, taskService.getTasks(sessionIdentifier, false).size());

		taskService.deleteTask(sessionIdentifier, taskIdentifier);

		assertEquals(0, taskService.getTasks(sessionIdentifier, false).size());
	}

	@Test
	public void testDeleteTask() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		assertEquals(0, taskService.getTasks(sessionIdentifier, false).size());

		final TaskDto taskDtoA = new TaskDto();
		taskDtoA.setName("nameA");
		taskDtoA.setDescription("descriptionA");
		taskDtoA.setFocus(TaskFocus.INBOX);
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

		assertEquals(3, taskService.getTasks(sessionIdentifier, false).size());

		{
			final Task taskA = taskService.getTask(sessionIdentifier, taskIdentifierA);
			assertNull(taskA.getParentId());
			final Task taskB = taskService.getTask(sessionIdentifier, taskIdentifierB);
			assertEquals(taskA.getId(), taskB.getParentId());
			final Task taskC = taskService.getTask(sessionIdentifier, taskIdentifierC);
			assertEquals(taskB.getId(), taskC.getParentId());
		}

		taskService.deleteTask(sessionIdentifier, taskIdentifierB);
		assertEquals(2, taskService.getTasks(sessionIdentifier, false).size());

		{
			final Task taskA = taskService.getTask(sessionIdentifier, taskIdentifierA);
			assertNull(taskA.getParentId());
			final Task taskB = taskService.getTask(sessionIdentifier, taskIdentifierB);
			assertNull(taskB);
			final Task taskC = taskService.getTask(sessionIdentifier, taskIdentifierC);
			assertEquals(taskA.getId(), taskC.getParentId());
		}

		taskService.deleteTask(sessionIdentifier, taskIdentifierA);
		assertEquals(1, taskService.getTasks(sessionIdentifier, false).size());

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

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		assertEquals(0, taskService.getTasks(sessionIdentifier, false).size());

		final TaskDto taskDtoA = new TaskDto();
		taskDtoA.setName("nameA");
		taskDtoA.setDescription("descriptionA");
		taskDtoA.setFocus(TaskFocus.INBOX);

		final TaskIdentifier taskIdentifierA = taskService.createTask(sessionIdentifier, taskDtoA);
		assertNotNull(taskIdentifierA);

		final TaskDto taskDtoB = new TaskDto();
		taskDtoB.setName("nameA");
		taskDtoB.setDescription("descriptionA");
		taskDtoB.setParentId(taskIdentifierA);

		final TaskIdentifier taskIdentifierB = taskService.createTask(sessionIdentifier, taskDtoB);
		assertNotNull(taskIdentifierB);

		assertEquals(2, taskService.getTasks(sessionIdentifier, false).size());

		try {
			taskService.completeTask(sessionIdentifier, taskIdentifierA);
			fail("ValidationException expected");
		} catch (final ValidationException e) {
			assertNotNull(e);
		}

		assertEquals(2, taskService.getTasks(sessionIdentifier, false).size());

		taskService.completeTask(sessionIdentifier, taskIdentifierB);

		assertEquals(1, taskService.getTasks(sessionIdentifier, false).size());

		taskService.completeTask(sessionIdentifier, taskIdentifierA);

		assertEquals(0, taskService.getTasks(sessionIdentifier, false).size());

	}

	@Test
	public void testCreateChildStartDue() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);
		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		// both null
		{

			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setFocus(TaskFocus.INBOX);
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
			taskDtoA.setFocus(TaskFocus.INBOX);
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
			taskDtoA.setFocus(TaskFocus.INBOX);
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
			taskDtoA.setFocus(TaskFocus.INBOX);
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
			taskDtoA.setFocus(TaskFocus.INBOX);
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
		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		// both null
		{
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoA.setFocus(TaskFocus.INBOX);

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
		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		// both null
		{
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoC.setFocus(TaskFocus.INBOX);
			taskService.updateTask(sessionIdentifier, taskDtoC);

			final Task child = taskService.getTask(sessionIdentifier, childTaskIdentifier);
			assertNull(child.getStart());
			assertNull(child.getDue());
		}

		// parent null, child not
		{
			final TaskDto taskDtoA = new TaskDto();
			taskDtoA.setName("parent");
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoC.setFocus(TaskFocus.INBOX);
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
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoC.setFocus(TaskFocus.INBOX);
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
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoC.setFocus(TaskFocus.INBOX);
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
			taskDtoA.setFocus(TaskFocus.INBOX);

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
			taskDtoC.setFocus(TaskFocus.INBOX);
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

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		{
			final TaskDto taskDto = new TaskDto();
			taskDto.setName("task");
			taskDto.setFocus(TaskFocus.INBOX);

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

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		final TaskDto taskDtoOrg = new TaskDto();
		taskDtoOrg.setName("task");
		taskDtoOrg.setFocus(TaskFocus.INBOX);

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
			taskDto.setFocus(TaskFocus.SOMEDAY);
			taskService.updateTask(sessionIdentifier, taskDto);
		}

		{
			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			assertEquals(TaskFocus.SOMEDAY, task.getFocus());
		}
	}

	@Test
	public void testGetTasksCompleted() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);

		final List<TaskContextIdentifier> taskContextIdentifiers = new ArrayList<>();
		assertNotNull(taskService.getTasks(sessionIdentifier, true, taskContextIdentifiers));
	}

	@Test
	public void testDeleteContext() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);
		final String taskContextName = "testContext";
		final String taskName = "testName";

		final TaskContextIdentifier taskContextId = taskService.createTaskContext(sessionIdentifier, taskContextName);

		final TaskDto taskDto = new TaskDto();
		taskDto.setName(taskName);
		taskDto.setContext(taskContextId);
		taskDto.setFocus(TaskFocus.INBOX);
		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, taskDto);

		// before delete
		{
			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			assertNotNull(task);
			assertNotNull(task.getContext());
			assertEquals(taskContextId, task.getContext());
		}

		taskService.deleteContextTask(sessionIdentifier, taskContextId);

		// after delete
		{
			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			assertNotNull(task);
			assertNull(task.getContext());
		}
	}

	@Test
	public void testCreateTask() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);
		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);
		final TaskDto task = new TaskDto();
		task.setFocus(TaskFocus.INBOX);
		task.setName(TASK_NAME);
		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, task);
		assertThat(taskIdentifier, is(not(nullValue())));
	}

	private TaskIdentifier getTask(final SessionIdentifier sessionIdentifier, final TaskService taskService) throws ValidationException, PermissionDeniedException, LoginRequiredException, TaskServiceException {
		final TaskDto task = new TaskDto();
		task.setFocus(TaskFocus.INBOX);
		task.setName(TASK_NAME);
		return taskService.createTask(sessionIdentifier, task);
	}

	@Test
	public void testGetAttachments() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);

		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);
		final TaskIdentifier taskIdentifier = getTask(sessionIdentifier, taskService);

		{
			final Collection<TaskAttachmentIdentifier> attachments = taskService.getAttachmentIdentifiers(sessionIdentifier, taskIdentifier);
			assertThat(attachments, is(not(nullValue())));
			assertThat(attachments.size(), is(0));
		}
		{
			final TaskAttachmentDto taskAttachment = new TaskAttachmentDto();
			taskAttachment.setName("taskAttachmentName");
			taskAttachment.setTask(taskIdentifier);
			final TaskAttachmentIdentifier taskAttachmentIdentifier = taskService.addAttachment(sessionIdentifier, taskAttachment);
			assertThat(taskAttachmentIdentifier, is(not(nullValue())));
		}
		{
			final Collection<TaskAttachmentIdentifier> attachments = taskService.getAttachmentIdentifiers(sessionIdentifier, taskIdentifier);
			assertThat(attachments, is(not(nullValue())));
			assertThat(attachments.size(), is(1));
		}

		final SessionIdentifier secondSessionIdentifier = getLoginSession(authenticationService, "secondUser");
		final TaskIdentifier secondTaskIdentifier = getTask(secondSessionIdentifier, taskService);
		final TaskAttachmentDto secondTaskAttachment = new TaskAttachmentDto();
		secondTaskAttachment.setName("taskAttachmentName");
		secondTaskAttachment.setTask(secondTaskIdentifier);
		final TaskAttachmentIdentifier secondTaskAttachmentIdentifier = taskService.addAttachment(secondSessionIdentifier, secondTaskAttachment);
		assertThat(secondTaskAttachmentIdentifier, is(not(nullValue())));

		{
			final Collection<TaskAttachmentIdentifier> attachments = taskService.getAttachmentIdentifiers(secondSessionIdentifier, secondTaskIdentifier);
			assertThat(attachments, is(not(nullValue())));
			assertThat(attachments.size(), is(1));
		}
	}

	@Test
	public void testAddAttachment() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final AuthenticationService authenticationService = injector.getInstance(AuthenticationService.class);
		final TaskService taskService = injector.getInstance(TaskService.class);
		final SessionIdentifier sessionIdentifier = getLoginSession(authenticationService, USERNAME);
		final TaskIdentifier taskIdentifier = getTask(sessionIdentifier, taskService);

		{
			try {
				final TaskAttachmentDto taskAttachment = new TaskAttachmentDto();
				taskService.addAttachment(sessionIdentifier, taskAttachment);
				fail("ValidationException expected");
			} catch (ValidationException e) {
				assertThat(e, is(not(nullValue())));
			}
		}
		{
			try {
				final TaskAttachmentDto taskAttachment = new TaskAttachmentDto();
				taskAttachment.setName("taskAttachmentName");
				taskService.addAttachment(sessionIdentifier, taskAttachment);
				fail("ValidationException expected");
			} catch (ValidationException e) {
				assertThat(e, is(not(nullValue())));
			}
		}
		{
			final TaskAttachmentDto taskAttachment = new TaskAttachmentDto();
			taskAttachment.setName("taskAttachmentName");
			taskAttachment.setTask(taskIdentifier);
			final TaskAttachmentIdentifier taskAttachmentIdentifier = taskService.addAttachment(sessionIdentifier, taskAttachment);
			assertThat(taskAttachmentIdentifier, is(not(nullValue())));
		}
	}

}
