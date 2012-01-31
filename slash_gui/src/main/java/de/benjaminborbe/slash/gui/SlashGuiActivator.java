package de.benjaminborbe.slash.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.slash.gui.guice.SlashGuiModules;
import de.benjaminborbe.slash.gui.servlet.SlashGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class SlashGuiActivator extends HttpBundleActivator {

	@Inject
	private SlashGuiServlet slashGuiServlet;

	public SlashGuiActivator() {
		super("slash");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SlashGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(slashGuiServlet, "/"));
		return result;
	}

}
