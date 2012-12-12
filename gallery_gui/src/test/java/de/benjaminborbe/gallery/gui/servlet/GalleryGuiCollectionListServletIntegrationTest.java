package de.benjaminborbe.gallery.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.gallery.gui.guice.GalleryGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GalleryGuiCollectionListServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryGuiModulesMock());
		final GalleryGuiCollectionListServlet a = injector.getInstance(GalleryGuiCollectionListServlet.class);
		final GalleryGuiCollectionListServlet b = injector.getInstance(GalleryGuiCollectionListServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
