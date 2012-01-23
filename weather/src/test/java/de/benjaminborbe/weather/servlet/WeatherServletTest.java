package de.benjaminborbe.weather.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.weather.guice.WeatherModulesMock;

public class WeatherServletTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WeatherModulesMock());
		final WeatherServlet a = injector.getInstance(WeatherServlet.class);
		final WeatherServlet b = injector.getInstance(WeatherServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
