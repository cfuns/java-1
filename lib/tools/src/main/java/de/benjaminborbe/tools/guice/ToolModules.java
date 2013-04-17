package de.benjaminborbe.tools.guice;

import com.google.inject.Module;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ToolModules implements Modules {

	@Override
	public Collection<Module> getModules() {
		final Set<Module> result = new HashSet<>();
		result.add(new ToolModule());
		return result;
	}

}
