package de.benjaminborbe.cron;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.cron.guice.CronModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CronActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CronModulesMock());
		final CronActivator o = injector.getInstance(CronActivator.class);
		assertNotNull(o);
	}
}
