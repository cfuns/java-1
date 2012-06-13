package de.benjaminborbe.monitoring.util;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.api.CheckResult;

public class MonitoringMailerTest {

	@Test
	public void testBuildMailContentEmpty() throws Exception {
		final MonitoringMailer monitoringCronJob = new MonitoringMailer(null, null, null, null);
		final Collection<CheckResult> failedChecks = new ArrayList<CheckResult>();
		assertEquals("Checks failed: 0\n\nhttp://bb/bb/monitoring\n", monitoringCronJob.buildMailContent(failedChecks));
	}

	@Test
	public void testBuildMailContentOneFailedCheck() throws Exception {
		final MonitoringMailer monitoringCronJob = new MonitoringMailer(null, null, null, null);
		final Collection<CheckResult> failedChecks = new ArrayList<CheckResult>();

		final Check check = EasyMock.createMock(Check.class);
		EasyMock.expect(check.getName()).andReturn("MyCheck");
		EasyMock.replay(check);

		final CheckResult checkResult = EasyMock.createMock(CheckResult.class);
		EasyMock.expect(checkResult.getUrl()).andReturn(new URL("http://www.test.de"));
		EasyMock.expect(checkResult.getMessage()).andReturn("foo");
		EasyMock.expect(checkResult.getCheck()).andReturn(check);
		EasyMock.replay(checkResult);

		failedChecks.add(checkResult);
		assertEquals("Checks failed: 1\nMyCheck: foo http://www.test.de\n\nhttp://bb/bb/monitoring\n", monitoringCronJob.buildMailContent(failedChecks));
	}
}
