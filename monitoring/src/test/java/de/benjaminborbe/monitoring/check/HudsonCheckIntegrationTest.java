package de.benjaminborbe.monitoring.check;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class HudsonCheckIntegrationTest {

	@Ignore
	@Test
	public void testUnit() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final String name = "Hudson-Check on TwentyFeet-UnitTests";
		final String url = "https://hudson.rp.seibert-media.net/";
		final String job = "20ft UnitTests";
		final HudsonCheckBuilder hudsonCheckBuilder = injector.getInstance(HudsonCheckBuilder.class);
		final MonitoringCheck check = hudsonCheckBuilder.buildCheck(name, url, job);
		final MonitoringCheckResult checkResult = check.check();
		assertNotNull(checkResult);
		assertTrue(checkResult.isSuccess());
	}

	@Ignore
	@Test
	public void testIntegration() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final String name = "Hudson-Check on Twentyfeet-IntegrationTest";
		final String url = "https://hudson.rp.seibert-media.net/";
		final String job = "20ft IntegrationTest";
		final HudsonCheckBuilder hudsonCheckBuilder = injector.getInstance(HudsonCheckBuilder.class);
		final MonitoringCheck check = hudsonCheckBuilder.buildCheck(name, url, job);
		final MonitoringCheckResult checkResult = check.check();
		assertNotNull(checkResult);
		assertTrue(checkResult.isSuccess());
	}
}
