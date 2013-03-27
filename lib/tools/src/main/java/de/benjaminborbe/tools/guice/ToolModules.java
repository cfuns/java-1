package de.benjaminborbe.tools.guice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Module;

public class ToolModules implements Modules {

	@Override
	public Collection<Module> getModules() {
		final Set<Module> result = new HashSet<Module>();
		result.add(new ToolModule());
		return result;
	}

}
