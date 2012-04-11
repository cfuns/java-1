package de.benjaminborbe.monitoring.check.twentyfeet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.check.twentyfeet.TwentyfeetQueueCheck;
import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TwentyfeetQueueCheckTest {

	@Test
	public void testInjector() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		assertNotNull(injector.getInstance(TwentyfeetQueueCheck.class));
	}

	@Ignore("slow")
	@Test
	public void testCheck() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final Check check = injector.getInstance(TwentyfeetQueueCheck.class);
		final CheckResult result = check.check();
		assertNotNull(result);
		assertTrue(result.getMessage(), result.isSuccess());
	}

	@Test
	public void testbuildResult() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StringWriter sw = new StringWriter();
		sw.append("<pre>");
		sw.append("service;missing;total");
		sw.append("googleanalytics;191;370");
		sw.append("bitly;91;206");
		sw.append("facebook;1308;2017");
		sw.append("youtube;162;306");
		sw.append("myspace;10;20");
		sw.append("internal;1;9");
		sw.append("Twitter;4475;6691");
		sw.append("</pre>");

		final TwentyfeetQueueCheck check = new TwentyfeetQueueCheck(logger, null, null, null, null);
		final CheckResult result = check.buildResult(sw.toString(), sw.toString(), null);
		assertNotNull(result);
	}
}
