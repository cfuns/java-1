package de.benjaminborbe.gallery.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.gallery.gui.guice.GalleryGuiModulesMock;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GalleryGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryGuiModulesMock());
		final GalleryGuiServlet a = injector.getInstance(GalleryGuiServlet.class);
		final GalleryGuiServlet b = injector.getInstance(GalleryGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
