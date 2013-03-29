package de.benjaminborbe.virt.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.virt.gui.guice.VirtGuiModules;
import de.benjaminborbe.virt.gui.servlet.VirtGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class VirtGuiActivator extends HttpBundleActivator {

	@Inject
	private VirtGuiServlet virtGuiServlet;

	public VirtGuiActivator() {
		super(VirtGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new VirtGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(virtGuiServlet, VirtGuiConstants.URL_HOME));
		return result;
	}

}
