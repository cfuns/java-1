package de.benjaminborbe.task.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.task.gui.guice.TaskGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskGuiTasksUncompletedServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiTasksUncompletedServlet a = injector.getInstance(TaskGuiTasksUncompletedServlet.class);
		final TaskGuiTasksUncompletedServlet b = injector.getInstance(TaskGuiTasksUncompletedServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
