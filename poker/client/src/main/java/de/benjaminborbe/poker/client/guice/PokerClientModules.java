package de.benjaminborbe.poker.client.guice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Module;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;

public class PokerClientModules implements Modules {

	@Override
	public Collection<Module> getModules() {
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new PokerClientModule());
		modules.add(new ToolModule());
		return modules;
	}
}
