package de.benjaminborbe.googlesearch.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.googlesearch.gui.guice.GooglesearchGuiModules;
import de.benjaminborbe.googlesearch.gui.service.GooglesearchGuiSpecialSearch;
import de.benjaminborbe.googlesearch.gui.servlet.GooglesearchGuiServlet;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class GooglesearchGuiActivator extends HttpBundleActivator {

	@Inject
	private GooglesearchGuiServlet googlesearchGuiServlet;

	@Inject
	private GooglesearchGuiSpecialSearch searchGuiSpecialSearchGoogle;

	public GooglesearchGuiActivator() {
		super("googlesearch");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GooglesearchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(googlesearchGuiServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(SearchSpecial.class, searchGuiSpecialSearchGoogle, searchGuiSpecialSearchGoogle.getClass().getName()));
		return result;
	}

}
