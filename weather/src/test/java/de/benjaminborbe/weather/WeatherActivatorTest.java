package de.benjaminborbe.weather;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.weather.WeatherActivator;
import de.benjaminborbe.weather.guice.WeatherModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class WeatherActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WeatherModulesMock());
		final WeatherActivator o = injector.getInstance(WeatherActivator.class);
		assertNotNull(o);
	}

}
