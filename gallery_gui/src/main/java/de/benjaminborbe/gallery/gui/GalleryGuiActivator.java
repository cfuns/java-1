package de.benjaminborbe.gallery.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.gallery.gui.guice.GalleryGuiModules;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiCollectionCreateServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiCollectionDeleteServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiCollectionUpdateServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiEntryDeleteServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiEntryUpdateServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiGroupCreateServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiGroupDeleteServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiGroupListServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiGroupUpdateServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiImageServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiEntryListServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiCollectionListServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiEntryCreateServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntryImpl;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class GalleryGuiActivator extends HttpBundleActivator {

	@Inject
	private GalleryGuiCollectionUpdateServlet galleryGuiCollectionUpdateServlet;

	@Inject
	private GalleryGuiEntryUpdateServlet galleryGuiEntryUpdateServlet;

	@Inject
	private GalleryGuiGroupUpdateServlet galleryGuiGroupUpdateServlet;

	@Inject
	private GalleryGuiCollectionCreateServlet galleryGuiCollectionCreateServlet;

	@Inject
	private GalleryGuiCollectionDeleteServlet galleryGuiCollectionDeleteServlet;

	@Inject
	private GalleryGuiCollectionListServlet galleryGuiCollectionListServlet;

	@Inject
	private GalleryGuiEntryCreateServlet galleryGuiEntryCreateServlet;

	@Inject
	private GalleryGuiEntryDeleteServlet galleryGuiEntryDeleteServlet;

	@Inject
	private GalleryGuiEntryListServlet galleryGuiEntryListServlet;

	@Inject
	private GalleryGuiImageServlet galleryGuiImageServlet;

	@Inject
	private GalleryGuiGroupCreateServlet galleryGuiGroupCreateServlet;

	@Inject
	private GalleryGuiGroupDeleteServlet galleryGuiGroupDeleteServlet;

	@Inject
	private GalleryGuiGroupListServlet galleryGuiGroupListServlet;

	public GalleryGuiActivator() {
		super(GalleryGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GalleryGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(galleryGuiGroupCreateServlet, GalleryGuiConstants.URL_GROUP_CREATE));
		result.add(new ServletInfo(galleryGuiGroupDeleteServlet, GalleryGuiConstants.URL_GROUP_DELETE));
		result.add(new ServletInfo(galleryGuiGroupListServlet, GalleryGuiConstants.URL_GROUP_LIST));
		result.add(new ServletInfo(galleryGuiCollectionCreateServlet, GalleryGuiConstants.URL_COLLECTION_CREATE));
		result.add(new ServletInfo(galleryGuiCollectionDeleteServlet, GalleryGuiConstants.URL_COLLECTION_DELETE));
		result.add(new ServletInfo(galleryGuiCollectionListServlet, GalleryGuiConstants.URL_COLLECTION_LIST));
		result.add(new ServletInfo(galleryGuiEntryCreateServlet, GalleryGuiConstants.URL_ENTRY_CREATE));
		result.add(new ServletInfo(galleryGuiEntryDeleteServlet, GalleryGuiConstants.URL_ENTRY_DELETE));
		result.add(new ServletInfo(galleryGuiEntryListServlet, GalleryGuiConstants.URL_ENTRY_LIST));
		result.add(new ServletInfo(galleryGuiImageServlet, GalleryGuiConstants.URL_IMAGE));
		result.add(new ServletInfo(galleryGuiCollectionUpdateServlet, GalleryGuiConstants.URL_COLLECTION_UPDATE));
		result.add(new ServletInfo(galleryGuiEntryUpdateServlet, GalleryGuiConstants.URL_ENTRY_UPDATE));
		result.add(new ServletInfo(galleryGuiGroupUpdateServlet, GalleryGuiConstants.URL_GROUP_UPDATE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, new NavigationEntryImpl("Gallery", "/bb/" + GalleryGuiConstants.NAME)));
		return result;
	}

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(galleryFilter, ".*", 998));
	// return result;
	// }

	// @Override
	// protected Collection<ResourceInfo> getResouceInfos() {
	// final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
	// // result.add(new ResourceInfo("/css", "css"));
	// // result.add(new ResourceInfo("/js", "js"));
	// // result.add(new ResourceInfo("/images", "images"));
	// return result;
	// }
}
