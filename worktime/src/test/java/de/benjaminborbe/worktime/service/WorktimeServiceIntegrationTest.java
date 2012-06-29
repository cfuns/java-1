package de.benjaminborbe.worktime.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.guice.WorktimeModulesMock;

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
