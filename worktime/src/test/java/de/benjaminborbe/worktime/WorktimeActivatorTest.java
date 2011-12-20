package de.benjaminborbe.worktime;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.worktime.guice.WorktimeModulesMock;

public class WorktimeActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		final WorktimeActivator o = injector.getInstance(WorktimeActivator.class);
		assertNotNull(o);
	}
}
