package de.benjaminborbe.task.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.task.api.Task;

public class TaskServiceImplUnitTest {

	@Test
	public void testSortAndLimit() {
		final TaskServiceImpl taskServiceImpl = new TaskServiceImpl(null, null, null, null, null, null, null, null, null, null);
		{
			final List<Task> list = new ArrayList<Task>();
			final List<Task> result = taskServiceImpl.sortAndLimit(list, 1);
			assertNotNull(result);
			assertEquals(0, result.size());
		}
		{
			final List<Task> list = new ArrayList<Task>();
			list.add(buildTask(1));
			final List<Task> result = taskServiceImpl.sortAndLimit(list, 1);
			assertNotNull(result);
			assertEquals(1, result.size());
		}
		{
			final List<Task> list = new ArrayList<Task>();
			list.add(buildTask(null));
			final List<Task> result = taskServiceImpl.sortAndLimit(list, 1);
			assertNotNull(result);
			assertEquals(1, result.size());
		}
		{
			final List<Task> list = new ArrayList<Task>();
			list.add(buildTask(2));
			list.add(buildTask(1));
			list.add(buildTask(3));
			list.add(buildTask(4));
			final List<Task> result = taskServiceImpl.sortAndLimit(list, 2);
			assertNotNull(result);
			assertEquals(2, result.size());
			assertEquals(new Integer(1), list.get(0).getPriority());
			assertEquals(new Integer(2), list.get(1).getPriority());
		}
		{
			final List<Task> list = new ArrayList<Task>();
			list.add(buildTask(2));
			list.add(buildTask(1));
			final List<Task> result = taskServiceImpl.sortAndLimit(list, 1);
			assertNotNull(result);
			assertEquals(1, result.size());
			assertEquals(new Integer(1), list.get(0).getPriority());
		}
	}

	private Task buildTask(final Integer i) {
		final Task task = EasyMock.createMock(Task.class);
		EasyMock.expect(task.getPriority()).andReturn(i).anyTimes();
		EasyMock.replay(task);
		return task;
	}
}
