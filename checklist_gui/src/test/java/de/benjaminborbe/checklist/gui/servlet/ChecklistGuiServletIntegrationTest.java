package de.benjaminborbe.checklist.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.checklist.gui.guice.ChecklistGuiModulesMock;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class ChecklistGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ChecklistGuiModulesMock());
		final ChecklistGuiServlet a = injector.getInstance(ChecklistGuiServlet.class);
		final ChecklistGuiServlet b = injector.getInstance(ChecklistGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
