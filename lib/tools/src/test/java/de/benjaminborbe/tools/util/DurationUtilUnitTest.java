package de.benjaminborbe.tools.util;

import com.google.inject.Provider;
import de.benjaminborbe.tools.guice.ProviderAdapter;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DurationUtilUnitTest {

	@Test
	public void testGetDuration() {
		final Duration duration = EasyMock.createMock(Duration.class);
		EasyMock.replay(duration);

		final Provider<Duration> p = new ProviderAdapter<>(duration);
		final DurationUtil durationUtil = new DurationUtil(p);
		assertNotNull(durationUtil.getDuration());
		assertEquals(duration, durationUtil.getDuration());
	}
}
