package de.benjaminborbe.tools.date;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateUtilIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final DateUtil a = injector.getInstance(DateUtil.class);
		final DateUtil b = injector.getInstance(DateUtil.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
