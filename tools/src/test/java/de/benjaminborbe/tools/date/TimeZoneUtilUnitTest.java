package de.benjaminborbe.tools.date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TimeZoneUtilUnitTest {

	@Test
	public void testgetUTCTimeZone() {
		final TimeZoneUtil t = new TimeZoneUtilImpl();
		assertNotNull(t.getUTCTimeZone());
		assertEquals("GMT", t.getUTCTimeZone().getID());
	}
}
