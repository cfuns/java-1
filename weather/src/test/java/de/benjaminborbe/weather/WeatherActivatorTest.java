package de.benjaminborbe.weather;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.weather.guice.WeatherModulesMock;

public class WeatherActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WeatherModulesMock());
		final WeatherActivator o = injector.getInstance(WeatherActivator.class);
		assertNotNull(o);
	}

}
