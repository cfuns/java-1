package de.benjaminborbe.monitoring.check;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class TcpCheckTest {

	@Test
	public void check() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		// success
		{
			final TcpCheck check = new TcpCheck(logger, "Check1", "www.google.de", 80);
			final CheckResult result = check.check();
			assertTrue(result.isSuccess());
			assertEquals("Check1", check.getName());
		}
		// fail
		{
			final TcpCheck check = new TcpCheck(logger, "Check2", "www.google.de", 1337);
			final CheckResult result = check.check();
			assertFalse(result.isSuccess());
			assertEquals("Check1", check.getName());
		}
	}
}
