package de.benjaminborbe.gwt.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.gwt.gui.guice.GwtGuiModules;
import de.benjaminborbe.gwt.gui.servlet.GwtGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class GwtGuiActivator extends HttpBundleActivator {

	@Inject
	private GwtGuiServlet gwtGuiServlet;

	public GwtGuiActivator() {
		super("gwt");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GwtGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(gwtGuiServlet, "/"));
		return result;
	}

}
