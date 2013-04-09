package de.benjaminborbe.poker.table.server.guice;

import java.util.Arrays;
import java.util.Collection;

import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;

public class PokerTableServerModules implements Modules {

	private final BundleContext context;

	public PokerTableServerModules(final BundleContext context) {
		this.context = context;
	}

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(Peaberry.osgiModule(context), new ServletModule(), new PokerTableServerOsgiModule(), new PokerTableServerModule(), new ToolModule());
	}

}
