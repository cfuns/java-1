package de.benjaminborbe.tools.date;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;

public class TimeZoneUtilIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final TimeZoneUtil a = injector.getInstance(TimeZoneUtil.class);
		final TimeZoneUtil b = injector.getInstance(TimeZoneUtil.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
