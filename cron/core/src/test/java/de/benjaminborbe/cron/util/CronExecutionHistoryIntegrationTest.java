package de.benjaminborbe.cron.util;

import com.google.inject.Injector;
import de.benjaminborbe.cron.guice.CronModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CronExecutionHistoryIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CronModulesMock());
		assertNotNull(injector.getInstance(CronExecutionHistory.class));
	}
}
