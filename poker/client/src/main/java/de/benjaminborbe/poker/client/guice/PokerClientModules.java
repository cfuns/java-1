package de.benjaminborbe.poker.client.guice;

import com.google.inject.Module;
import de.benjaminborbe.httpdownloader.core.guice.HttpdownloaderCoreModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PokerClientModules implements Modules {

	@Override
	public Collection<Module> getModules() {
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new PokerClientModule());
		modules.add(new ToolModule());
		modules.add(new HttpdownloaderCoreModule());
		return modules;
	}
}
