package de.benjaminborbe.tools.date;

import de.benjaminborbe.tools.util.ParseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class TimeZoneUtilImplUnitTest {

	@Test
	public void testParse() throws Exception {
		final TimeZoneUtilImpl t = new TimeZoneUtilImpl();

		for (final String id : Arrays.asList("UTC", "GMT", "Europe/Berlin")) {
			final TimeZone timezone = t.parseTimeZone(id);
			assertEquals(id, timezone.getID());
		}

		try {
			t.parseTimeZone(null);
			fail("ParseException expected");
		} catch (final ParseException e) {
			assertNotNull(e);
		}

		try {
			t.parseTimeZone("");
			fail("ParseException expected");
		} catch (final ParseException e) {
			assertNotNull(e);
		}

		try {
			t.parseTimeZone("foo bar");
			fail("ParseException expected");
		} catch (final ParseException e) {
			assertNotNull(e);
		}
	}
}
