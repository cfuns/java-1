package de.benjaminborbe.worktime.core.service;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.core.guice.WorktimeModulesMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WorktimeServiceIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		final WorktimeService a = injector.getInstance(WorktimeService.class);
		final WorktimeService b = injector.getInstance(WorktimeService.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
