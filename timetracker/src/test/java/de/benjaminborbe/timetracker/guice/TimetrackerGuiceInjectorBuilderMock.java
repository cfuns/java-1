package de.benjaminborbe.timetracker.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.benjaminborbe.timetracker.guice.TimetrackerModule;

public class TimetrackerGuiceInjectorBuilderMock {

	private TimetrackerGuiceInjectorBuilderMock() {
	}

	public static Injector getInjector() {
		return Guice.createInjector(new PeaberryModuleMock(), new ServletModuleMock(), new TimetrackerOsgiModuleMock(),
				new TimetrackerModule());
	}
}
