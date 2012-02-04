package de.benjaminborbe.weather.gui.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.weather.gui.guice.WeatherGuiModulesMock;

public class WeatherGuiServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WeatherGuiModulesMock());
		final WeatherGuiServlet a = injector.getInstance(WeatherGuiServlet.class);
		final WeatherGuiServlet b = injector.getInstance(WeatherGuiServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
