package de.benjaminborbe.timetracker;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.timetracker.guice.TimetrackerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TimetrackerActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TimetrackerModulesMock());
		final TimetrackerActivator o = injector.getInstance(TimetrackerActivator.class);
		assertNotNull(o);
	}
}
