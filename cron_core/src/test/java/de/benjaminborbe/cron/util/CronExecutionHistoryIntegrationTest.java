package de.benjaminborbe.cron.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.cron.guice.CronModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CronExecutionHistoryIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CronModulesMock());
		assertNotNull(injector.getInstance(CronExecutionHistory.class));
	}
}
