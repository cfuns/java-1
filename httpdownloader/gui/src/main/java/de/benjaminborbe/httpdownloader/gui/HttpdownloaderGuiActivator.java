package de.benjaminborbe.httpdownloader.gui;

import de.benjaminborbe.httpdownloader.gui.guice.HttpdownloaderGuiModules;
import de.benjaminborbe.httpdownloader.gui.servlet.HttpdownloaderGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HttpdownloaderGuiActivator extends HttpBundleActivator {

	@Inject
	private HttpdownloaderGuiServlet httpdownloaderGuiServlet;

	public HttpdownloaderGuiActivator() {
		super(HttpdownloaderGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new HttpdownloaderGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(httpdownloaderGuiServlet, HttpdownloaderGuiConstants.URL_HOME));
		return result;
	}

}
