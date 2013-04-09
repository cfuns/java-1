package de.benjaminborbe.note.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.note.gui.guice.NoteGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NoteGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NoteGuiModulesMock());
		final NoteGuiServlet a = injector.getInstance(NoteGuiServlet.class);
		final NoteGuiServlet b = injector.getInstance(NoteGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
