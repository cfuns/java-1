package de.benjaminborbe.monitoring.check.twentyfeet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import com.google.inject.Injector;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TwentyfeetQueueCheckIntegrationTest {

	@Test
	public void testInjector() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		assertNotNull(injector.getInstance(TwentyfeetQueueCheck.class));
	}

	@Test
	@Ignore("slow")
	public void testCheck() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final Check check = injector.getInstance(TwentyfeetQueueCheck.class);
		final CheckResult result = check.check();
		assertNotNull(result);
		assertTrue(result.getMessage(), result.isSuccess());
	}

}
