package de.benjaminborbe.poker.guice;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import de.benjaminborbe.lib.validation.guice.ValidationModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import java.util.Arrays;
import java.util.Collection;

public class PokerModules implements Modules {

	private final BundleContext context;

	public PokerModules(final BundleContext context) {
		this.context = context;
	}

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(Peaberry.osgiModule(context), new ServletModule(), new PokerOsgiModule(), new PokerModule(), new ToolModule(), new ValidationModule());
	}
}
