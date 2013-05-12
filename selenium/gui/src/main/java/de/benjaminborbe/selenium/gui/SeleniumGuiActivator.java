package de.benjaminborbe.selenium.gui;

import de.benjaminborbe.selenium.gui.guice.SeleniumGuiModules;
import de.benjaminborbe.selenium.gui.servlet.SeleniumGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumGuiActivator extends HttpBundleActivator {

	@Inject
	private SeleniumGuiServlet seleniumGuiServlet;

	public SeleniumGuiActivator() {
		super(SeleniumGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SeleniumGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(seleniumGuiServlet, SeleniumGuiConstants.URL_HOME));
		return result;
	}

}
