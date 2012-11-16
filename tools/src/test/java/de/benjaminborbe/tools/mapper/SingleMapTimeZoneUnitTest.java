package de.benjaminborbe.tools.mapper;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.date.CurrentTime;

public class SingleMapTimeZoneUnitTest {

	public class T {

		private Calendar field;

		public Calendar getField() {
			return field;
		}

		public void setField(final Calendar field) {
			this.field = field;
		}

	}

	@Test
	public void testMap() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final String name = "field";

		final Collection<TimeZone> timeZones = Arrays.asList(TimeZone.getDefault(), null);
		for (final TimeZone timeZone : timeZones) {
			final SingleMapTimeZone<T> map = new SingleMapTimeZone<T>(name);
			final String timeZoneString = map.toString(timeZone);
			assertEquals(timeZone, map.fromString(timeZoneString));
		}
	}
}
