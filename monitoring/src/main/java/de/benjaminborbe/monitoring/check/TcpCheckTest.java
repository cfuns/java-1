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
			final TcpCheck check = new TcpCheck(logger, "www.google.de", 80);
			assertTrue(check.check());
		}
		// fail
		{
			final TcpCheck check = new TcpCheck(logger, "www.google.de", 1337);
			assertFalse(check.check());
		}
	}
}
