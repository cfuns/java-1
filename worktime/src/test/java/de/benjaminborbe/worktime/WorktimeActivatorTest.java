package de.benjaminborbe.worktime;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.worktime.WorktimeActivator;
import de.benjaminborbe.worktime.guice.WorktimeModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class WorktimeActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		final WorktimeActivator o = injector.getInstance(WorktimeActivator.class);
		assertNotNull(o);
	}
}
