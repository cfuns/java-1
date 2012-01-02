package de.benjaminborbe.weather.servlet;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.weather.guice.WeatherModulesMock;
import de.benjaminborbe.weather.servlet.WeatherServlet;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class WeatherServletTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WeatherModulesMock());
		final WeatherServlet a = injector.getInstance(WeatherServlet.class);
		final WeatherServlet b = injector.getInstance(WeatherServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void buildRedirectTargetPath() {
		final WeatherServlet weatherServlet = new WeatherServlet(null);
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getContextPath()).andReturn("/bb");
		EasyMock.replay(request);
		assertEquals("/bb/dashboard", weatherServlet.buildRedirectTargetPath(request));
	}
}
