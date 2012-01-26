package de.benjaminborbe.bookmark.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.bookmark.gui.guice.BookmarkGuiModules;
import de.benjaminborbe.bookmark.gui.service.BookmarkGuiFavoriteDashboardWidget;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiCreateServlet;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiDeleteServlet;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiListServlet;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiServlet;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiUpdateServlet;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class BookmarkGuiActivator extends HttpBundleActivator {

	@Inject
	private BookmarkGuiListServlet bookmark_guiListServlet;

	@Inject
	private BookmarkGuiCreateServlet bookmark_guiCreateServlet;

	@Inject
	private BookmarkGuiFavoriteDashboardWidget bookmark_guiFavoriteDashboardWidget;

	@Inject
	private BookmarkGuiServlet bookmark_guiServlet;

	@Inject
	private BookmarkGuiDeleteServlet bookmark_guiDeleteServlet;

	@Inject
	private BookmarkGuiUpdateServlet bookmark_guiUpdateServlet;

	public BookmarkGuiActivator() {
		super("bookmark_gui");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new BookmarkGuiModules(context);
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, bookmark_guiFavoriteDashboardWidget, bookmark_guiFavoriteDashboardWidget.getClass().getName()));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(bookmark_guiListServlet, "/list"));
		result.add(new ServletInfo(bookmark_guiCreateServlet, "/create"));
		result.add(new ServletInfo(bookmark_guiUpdateServlet, "/update"));
		result.add(new ServletInfo(bookmark_guiDeleteServlet, "/delete"));
		result.add(new ServletInfo(bookmark_guiServlet, "/"));
		return result;
	}

}
