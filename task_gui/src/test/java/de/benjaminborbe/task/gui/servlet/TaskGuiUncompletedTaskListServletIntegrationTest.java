package de.benjaminborbe.task.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.task.gui.guice.TaskGuiModulesMock;
import de.benjaminborbe.task.gui.servlet.TaskGuiUncompletedTaskListServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskGuiUncompletedTaskListServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiUncompletedTaskListServlet a = injector.getInstance(TaskGuiUncompletedTaskListServlet.class);
		final TaskGuiUncompletedTaskListServlet b = injector.getInstance(TaskGuiUncompletedTaskListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
