package de.benjaminborbe.vaadin.gui;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.vaadin.gui.guice.VaadinGuiModules;
import de.benjaminborbe.vaadin.gui.servlet.VaadinGuiApplicationServlet;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class VaadinGuiActivator extends HttpBundleActivator {

	@Inject
	private VaadinGuiApplicationServlet vaadinGuiApplicationServlet;

	public VaadinGuiActivator() {
		super(VaadinGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new VaadinGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(vaadinGuiApplicationServlet, "/"));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/VAADIN", "VAADIN", true));
		return result;
	}
}
