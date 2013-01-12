package de.benjaminborbe.dhl.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.dhl.gui.guice.DhlGuiModules;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiCreateServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiDeleteServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiListServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiSendStatusServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class DhlGuiActivator extends HttpBundleActivator {

	@Inject
	private DhlGuiServlet dhlGuiServlet;

	@Inject
	private DhlGuiSendStatusServlet dhlGuiSendStatusServlet;

	@Inject
	private DhlGuiCreateServlet dhlGuiCreateServlet;

	@Inject
	private DhlGuiDeleteServlet dhlGuiDeleteServlet;

	@Inject
	private DhlGuiListServlet dhlGuiListServlet;

	public DhlGuiActivator() {
		super(DhlGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DhlGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(dhlGuiServlet, "/"));
		result.add(new ServletInfo(dhlGuiSendStatusServlet, "/send"));
		result.add(new ServletInfo(dhlGuiCreateServlet, "/create"));
		result.add(new ServletInfo(dhlGuiDeleteServlet, "/delete"));
		result.add(new ServletInfo(dhlGuiListServlet, "/list"));
		return result;
	}


}
