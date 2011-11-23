package de.benjaminborbe.timetracker;

import com.google.inject.Injector;

import de.benjaminborbe.timetracker.TimetrackerActivator;
import de.benjaminborbe.timetracker.guice.TimeTrackerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import junit.framework.TestCase;

public class TimetrackerActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TimeTrackerModulesMock());
		final TimetrackerActivator o = injector.getInstance(TimetrackerActivator.class);
		assertNotNull(o);
	}
}
