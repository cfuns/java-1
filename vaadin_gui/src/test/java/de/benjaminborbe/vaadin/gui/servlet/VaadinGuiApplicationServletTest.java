package de.benjaminborbe.vaadin.gui.servlet;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.vaadin.gui.guice.VaadinGuiModulesMock;

public class VaadinGuiApplicationServletTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VaadinGuiModulesMock());
		final VaadinGuiApplicationServlet applicationServlet = injector.getInstance(VaadinGuiApplicationServlet.class);
		assertNotNull(applicationServlet);
	}

}
