package de.benjaminborbe.cron;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.cron.guice.CronModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CronActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CronModulesMock());
		final CronActivator o = injector.getInstance(CronActivator.class);
		assertNotNull(o);
	}
}
