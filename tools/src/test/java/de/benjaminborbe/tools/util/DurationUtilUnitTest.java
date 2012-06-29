package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Provider;

public class DurationUtilUnitTest {

	@Test
	public void testGetDuration() {
		final Duration duration = EasyMock.createMock(Duration.class);
		EasyMock.replay(duration);

		final Provider<Duration> p = new Provider<Duration>() {

			@Override
			public Duration get() {
				return duration;
			}
		};
		final DurationUtil durationUtil = new DurationUtil(p);
		assertNotNull(durationUtil.getDuration());
		assertEquals(duration, durationUtil.getDuration());
	}
}
