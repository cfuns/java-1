package de.benjaminborbe.tools.date;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;

public class CalendarUtilImplIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final CalendarUtil a = injector.getInstance(CalendarUtil.class);
		final CalendarUtil b = injector.getInstance(CalendarUtil.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

}
