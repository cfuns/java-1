package de.benjaminborbe.gwt.gui;

import javax.inject.Inject;
import de.benjaminborbe.gwt.gui.guice.GwtGuiModules;
import de.benjaminborbe.gwt.gui.servlet.GwtGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GwtGuiActivator extends HttpBundleActivator {

	@Inject
	private GwtGuiServlet gwtGuiServlet;

	public GwtGuiActivator() {
		super(GwtGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GwtGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(gwtGuiServlet, "/"));
		return result;
	}

}
