package de.benjaminborbe.tools.date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TimeZoneUtilUnitTest {

	@Test
	public void testgetUTCTimeZone() {
		final TimeZoneUtil t = new TimeZoneUtilImpl();
		assertNotNull(t.getUTCTimeZone());
		assertEquals("UTC", t.getUTCTimeZone().getID());
	}
}
