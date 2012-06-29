package de.benjaminborbe.monitoring.check.twentyfeet;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TwentyfeetChangeConnectorIntegrationTest {

	@Test
	@Ignore
	public void testRun() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final TwentyfeetChangeConnector twentyfeetChangeConnector = injector.getInstance(TwentyfeetChangeConnector.class);
		int last = 0;
		for (int i = 1; i <= 1; ++i) {
			final int n = twentyfeetChangeConnector.getMessagesConsumedTotal();
			if (last != 0)
				System.err.println("total = " + (n - last));
			last = n;
			Thread.sleep(1000);
		}
	}
}
