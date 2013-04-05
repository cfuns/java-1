package de.benjaminborbe.imagedownloader.gui.servlet;

import com.google.inject.Injector;
import de.benjaminborbe.imagedownloader.gui.guice.ImagedownloaderGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImagedownloaderGuiServletIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ImagedownloaderGuiModulesMock());
		final ImagedownloaderGuiServlet a = injector.getInstance(ImagedownloaderGuiServlet.class);
		final ImagedownloaderGuiServlet b = injector.getInstance(ImagedownloaderGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
