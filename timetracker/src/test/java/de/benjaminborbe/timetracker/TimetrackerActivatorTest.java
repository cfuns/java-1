package de.benjaminborbe.timetracker;

import com.google.inject.Injector;

import de.benjaminborbe.timetracker.TimetrackerActivator;
import de.benjaminborbe.timetracker.guice.TimetrackerGuiceInjectorBuilderMock;
import junit.framework.TestCase;

public class TimetrackerActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = TimetrackerGuiceInjectorBuilderMock.getInjector();
		final TimetrackerActivator o = injector.getInstance(TimetrackerActivator.class);
		assertNotNull(o);
	}
}
