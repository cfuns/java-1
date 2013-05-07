package de.benjaminborbe.googlesearch.gui;

import de.benjaminborbe.googlesearch.gui.guice.GooglesearchGuiModules;
import de.benjaminborbe.googlesearch.gui.service.GooglesearchGuiMapSpecialSearch;
import de.benjaminborbe.googlesearch.gui.service.GooglesearchGuiSpecialSearch;
import de.benjaminborbe.googlesearch.gui.service.GooglesearchGuiYoutubeSpecialSearch;
import de.benjaminborbe.googlesearch.gui.servlet.GooglesearchGuiServlet;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GooglesearchGuiActivator extends HttpBundleActivator {

	@Inject
	private GooglesearchGuiServlet googlesearchGuiServlet;

	@Inject
	private GooglesearchGuiYoutubeSpecialSearch googlesearchGuiYoutubeSpecialSearch;

	@Inject
	private GooglesearchGuiSpecialSearch searchGuiSpecialSearchGoogle;

	@Inject
	private GooglesearchGuiMapSpecialSearch googlesearchGuiMapSpecialSearch;

	public GooglesearchGuiActivator() {
		super(GooglesearchGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GooglesearchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(googlesearchGuiServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(SearchSpecial.class, searchGuiSpecialSearchGoogle, searchGuiSpecialSearchGoogle.getClass().getName()));
		result.add(new ServiceInfo(SearchSpecial.class, googlesearchGuiMapSpecialSearch, googlesearchGuiMapSpecialSearch.getClass().getName()));
		result.add(new ServiceInfo(SearchSpecial.class, googlesearchGuiYoutubeSpecialSearch, googlesearchGuiYoutubeSpecialSearch.getClass().getName()));
		return result;
	}

}
