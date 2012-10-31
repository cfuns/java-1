package de.benjaminborbe.task.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.task.gui.guice.TaskGuiModulesMock;
import de.benjaminborbe.task.gui.servlet.TaskGuiNextServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TaskGuiNextServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiNextServlet a = injector.getInstance(TaskGuiNextServlet.class);
		final TaskGuiNextServlet b = injector.getInstance(TaskGuiNextServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
