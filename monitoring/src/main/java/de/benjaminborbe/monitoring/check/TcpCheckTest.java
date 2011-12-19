package de.benjaminborbe.monitoring.check;

import org.easymock.EasyMock;
import org.slf4j.Logger;

import junit.framework.TestCase;

public class TcpCheckTest extends TestCase {

	public void testCheck() {
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
