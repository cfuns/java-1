package de.benjaminborbe.util.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.util.gui.guice.UtilGuiModules;
import de.benjaminborbe.util.gui.servlet.UtilGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class UtilGuiActivator extends HttpBundleActivator {

	@Inject
	private UtilGuiServlet utilGuiServlet;

	public UtilGuiActivator() {
		super("util");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new UtilGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(utilGuiServlet, "/"));
		return result;
	}

}
