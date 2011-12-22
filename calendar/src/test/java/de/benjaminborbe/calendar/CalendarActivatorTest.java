package de.benjaminborbe.calendar;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.calendar.CalendarActivator;
import de.benjaminborbe.calendar.guice.CalendarModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class CalendarActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new CalendarModulesMock());
		final CalendarActivator o = injector.getInstance(CalendarActivator.class);
		assertNotNull(o);
	}

}
