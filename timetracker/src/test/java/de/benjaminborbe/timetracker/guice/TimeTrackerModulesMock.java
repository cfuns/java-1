package de.benjaminborbe.timetracker.guice;

import java.util.Arrays;
import java.util.Collection;

import com.google.inject.Module;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.mock.PeaberryModuleMock;
import de.benjaminborbe.tools.mock.ServletModuleMock;

public class TimeTrackerModulesMock implements Modules {

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(new PeaberryModuleMock(), new ServletModuleMock(), new TimetrackerOsgiModuleMock(),
				new TimetrackerModule());
	}

}
