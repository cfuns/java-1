package de.benjaminborbe.task.gui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.util.ComparatorUtil;

public class TaskComparatorUnitTest {

	@Test
	public void testSort() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final TaskNameComparator name = new TaskNameComparator();
		final TaskPrioComparator prio = new TaskPrioComparator();
		final TaskComparator taskComparator = new TaskComparator(name, prio);
		{
			final List<Task> list = new ArrayList<Task>();
			final List<Task> result = comparatorUtil.sort(list, taskComparator);
			assertNotNull(result);
			assertEquals(0, result.size());
		}
		{
			final List<Task> list = new ArrayList<Task>();
			list.add(buildTask(1));
			final List<Task> result = comparatorUtil.sort(list, taskComparator);
			assertNotNull(result);
			assertEquals(1, result.size());
		}
		{
			final List<Task> list = new ArrayList<Task>();
			list.add(buildTask(null));
			final List<Task> result = comparatorUtil.sort(list, taskComparator);
			assertNotNull(result);
			assertEquals(1, result.size());
		}
		{
			final List<Task> list = new ArrayList<Task>();
			list.add(buildTask(2));
			list.add(buildTask(1));
			list.add(buildTask(3));
			list.add(buildTask(4));
			final List<Task> result = comparatorUtil.sort(list, taskComparator);
			assertNotNull(result);
			assertEquals(4, result.size());
			assertEquals(new Integer(1), list.get(0).getPriority());
			assertEquals(new Integer(2), list.get(1).getPriority());
			assertEquals(new Integer(3), list.get(2).getPriority());
			assertEquals(new Integer(4), list.get(3).getPriority());
		}
		{
			final List<Task> list = new ArrayList<Task>();
			list.add(buildTask(2));
			list.add(buildTask(1));
			final List<Task> result = comparatorUtil.sort(list, taskComparator);
			assertNotNull(result);
			assertEquals(2, result.size());
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
