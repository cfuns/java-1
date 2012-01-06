package de.benjaminborbe.timetracker;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.timetracker.guice.TimetrackerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TimetrackerActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TimetrackerModulesMock());
		final TimetrackerActivator o = injector.getInstance(TimetrackerActivator.class);
		assertNotNull(o);
	}
}
