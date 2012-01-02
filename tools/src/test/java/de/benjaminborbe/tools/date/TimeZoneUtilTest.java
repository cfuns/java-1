package de.benjaminborbe.tools.date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;

public class TimeZoneUtilTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final TimeZoneUtil a = injector.getInstance(TimeZoneUtil.class);
		final TimeZoneUtil b = injector.getInstance(TimeZoneUtil.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void getUTCTimeZone() {
		final TimeZoneUtil t = new TimeZoneUtilImpl();
		assertNotNull(t.getUTCTimeZone());
		assertEquals("GMT", t.getUTCTimeZone().getID());
	}
}
