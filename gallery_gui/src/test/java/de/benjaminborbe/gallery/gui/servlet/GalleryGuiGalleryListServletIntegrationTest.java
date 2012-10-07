package de.benjaminborbe.gallery.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.gallery.gui.guice.GalleryGuiModulesMock;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiGalleryListServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GalleryGuiGalleryListServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryGuiModulesMock());
		final GalleryGuiGalleryListServlet a = injector.getInstance(GalleryGuiGalleryListServlet.class);
		final GalleryGuiGalleryListServlet b = injector.getInstance(GalleryGuiGalleryListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
