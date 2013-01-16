package de.benjaminborbe.kiosk.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.kiosk.gui.guice.KioskGuiModules;
import de.benjaminborbe.kiosk.gui.servlet.KioskGuiBookServlet;
import de.benjaminborbe.kiosk.gui.servlet.KioskGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class KioskGuiActivator extends HttpBundleActivator {

	@Inject
	private KioskGuiBookServlet kioskGuiBookServlet;

	@Inject
	private KioskGuiServlet kioskGuiServlet;

	public KioskGuiActivator() {
		super(KioskGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new KioskGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(kioskGuiServlet, KioskGuiConstants.URL_HOME));
		result.add(new ServletInfo(kioskGuiBookServlet, KioskGuiConstants.URL_BOOK));
		return result;
	}

}
