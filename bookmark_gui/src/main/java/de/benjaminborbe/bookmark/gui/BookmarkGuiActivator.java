package de.benjaminborbe.bookmark.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.bookmark.gui.guice.BookmarkGuiModules;
import de.benjaminborbe.bookmark.gui.service.BookmarkGuiFavoriteDashboardWidget;
import de.benjaminborbe.bookmark.gui.service.BookmarkGuiSpecialSearch;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiCreateServlet;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiDeleteServlet;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiListServlet;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiServlet;
import de.benjaminborbe.bookmark.gui.servlet.BookmarkGuiUpdateServlet;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntryImpl;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class BookmarkGuiActivator extends HttpBundleActivator {

	@Inject
	private BookmarkGuiListServlet bookmarkGuiListServlet;

	@Inject
	private BookmarkGuiCreateServlet bookmarkGuiCreateServlet;

	@Inject
	private BookmarkGuiFavoriteDashboardWidget bookmarkGuiFavoriteDashboardWidget;

	@Inject
	private BookmarkGuiServlet bookmarkGuiServlet;

	@Inject
	private BookmarkGuiDeleteServlet bookmarkGuiDeleteServlet;

	@Inject
	private BookmarkGuiUpdateServlet bookmarkGuiUpdateServlet;

	@Inject
	private BookmarkGuiSpecialSearch searchGuiSpecialSearchBookmark;

	public BookmarkGuiActivator() {
		super(BookmarkGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new BookmarkGuiModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, bookmarkGuiFavoriteDashboardWidget, bookmarkGuiFavoriteDashboardWidget.getClass().getName()));
		result.add(new ServiceInfo(SearchSpecial.class, searchGuiSpecialSearchBookmark, searchGuiSpecialSearchBookmark.getClass().getName()));
		result.add(new ServiceInfo(NavigationEntry.class, new NavigationEntryImpl("Bookmark", "/bb/" + BookmarkGuiConstants.NAME + BookmarkGuiConstants.URL_LIST)));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(bookmarkGuiServlet, "/"));
		result.add(new ServletInfo(bookmarkGuiListServlet, BookmarkGuiConstants.URL_LIST));
		result.add(new ServletInfo(bookmarkGuiCreateServlet, BookmarkGuiConstants.URL_CREATE));
		result.add(new ServletInfo(bookmarkGuiUpdateServlet, BookmarkGuiConstants.URL_UPDATE));
		result.add(new ServletInfo(bookmarkGuiDeleteServlet, BookmarkGuiConstants.URL_DELETE));
		return result;
	}

}
