package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;

public class ParseUtilIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final ParseUtil a = injector.getInstance(ParseUtil.class);
		final ParseUtil b = injector.getInstance(ParseUtil.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
