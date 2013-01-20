package de.benjaminborbe.task.gui.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.gui.widget.TaskCache;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.tools.util.StringUtil;

public class TaskGuiUtilUnitTest {

	@Test
	public void testGetChildTasks() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		{
			final TaskCache taskCache = new TaskCache(logger, null);
			assertEquals(0, taskCache.getChildTasks(null).size());
		}
		{
			final TaskCache taskCache = new TaskCache(logger, null);
			taskCache.add(buildTask(new TaskIdentifier(1), null));
			assertEquals(1, taskCache.getChildTasks(null).size());
		}
		{
			final TaskCache taskCache = new TaskCache(logger, null);
			taskCache.add(buildTask(new TaskIdentifier(2), null));
			taskCache.add(buildTask(new TaskIdentifier(3), new TaskIdentifier(123)));
			assertEquals(1, taskCache.getChildTasks(null).size());
		}
		{
			final TaskCache taskCache = new TaskCache(logger, null);
			taskCache.add(buildTask(new TaskIdentifier(4), null));
			taskCache.add(buildTask(new TaskIdentifier(5), new TaskIdentifier(123)));
			assertEquals(1, taskCache.getChildTasks(new TaskIdentifier(123)).size());
		}
	}

	private Task buildTask(final TaskIdentifier id, final TaskIdentifier parentId) {
		final Task task = EasyMock.createMock(Task.class);
		EasyMock.expect(task.getId()).andReturn(id).anyTimes();
		EasyMock.expect(task.getParentId()).andReturn(parentId).anyTimes();
		EasyMock.replay(task);
		return task;
	}

	@Test
	public void testQuickStringToTask() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final SessionIdentifier sessionIdentifier = null;
		final StringUtil stringUtil = null;

		final TaskContextIdentifier id = EasyMock.createMock(TaskContextIdentifier.class);
		EasyMock.replay(id);

		final TaskContext taskContext = EasyMock.createMock(TaskContext.class);
		EasyMock.expect(taskContext.getId()).andReturn(id).anyTimes();
		EasyMock.expect(taskContext.getName()).andReturn("home").anyTimes();
		EasyMock.replay(taskContext);

		final TaskService taskService = EasyMock.createMock(TaskService.class);
		EasyMock.expect(taskService.getTaskContextByName(sessionIdentifier, "home")).andReturn(taskContext).anyTimes();
		EasyMock.replay(taskService);

		final ParseUtil parseUtil = new ParseUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final UrlUtil urlUtil = new UrlUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final TaskGuiUtil taskGuiUtil = new TaskGuiUtil(logger, taskService, stringUtil, calendarUtil, urlUtil, parseUtil);
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, null).getName(), is(nullValue()));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, "bla").getName(), is("bla"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla ").getName(), is("bla"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla: ").getName(), is("bla:"));

		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla url: http://www.google.de ").getName(), is("bla"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla url: http://www.google.de ").getUrl(), is("http://www.google.de"));

		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla focus: next ").getName(), is("bla"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla focus: next ").getFocus(), is(TaskFocus.NEXT));

		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla focus: bla ").getName(), is("bla"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla focus: bla ").getFocus(), is(TaskFocus.INBOX));

		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla context: home foo ").getName(), is("bla foo"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla context: home foo ").getContext(), is(taskContext.getId()));

		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla due: 0d foo ").getName(), is("bla foo"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla due: 0d foo ").getDue(), is(notNullValue()));

		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla start: 0d foo ").getName(), is("bla foo"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla start: 0d foo ").getStart(), is(notNullValue()));

		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla context: home due: 0d start: 0d foo ").getName(), is("bla foo"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla context: home due: 0d start: 0d foo ").getContext(), is(taskContext.getId()));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla context: home due: 0d start: 0d foo ").getName(), is("bla foo"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla context: home due: 0d start: 0d foo ").getDue(), is(notNullValue()));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla context: home due: 0d start: 0d foo ").getName(), is("bla foo"));
		assertThat(taskGuiUtil.quickStringToTask(sessionIdentifier, " bla context: home due: 0d start: 0d foo ").getStart(), is(notNullValue()));

	}
}
