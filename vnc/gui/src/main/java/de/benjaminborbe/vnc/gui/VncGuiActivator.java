package de.benjaminborbe.vnc.gui;

import com.google.inject.Inject;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.vnc.gui.guice.VncGuiModules;
import de.benjaminborbe.vnc.gui.servlet.VncGuiServlet;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class VncGuiActivator extends HttpBundleActivator {

	@Inject
	private VncGuiServlet vncGuiServlet;

	public VncGuiActivator() {
		super(VncGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new VncGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(vncGuiServlet, VncGuiConstants.URL_HOME));
		return result;
	}

}
