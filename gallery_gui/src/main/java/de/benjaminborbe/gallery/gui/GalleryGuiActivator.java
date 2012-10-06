package de.benjaminborbe.gallery.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.gallery.gui.guice.GalleryGuiModules;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiCreateServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiImageDeleteServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiImageServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiImageListServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiServlet;
import de.benjaminborbe.gallery.gui.servlet.GalleryGuiImageUploadServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntryImpl;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class GalleryGuiActivator extends HttpBundleActivator {

	@Inject
	private GalleryGuiCreateServlet galleryGuiCreateServlet;

	@Inject
	private GalleryGuiServlet galleryGuiServlet;

	@Inject
	private GalleryGuiImageUploadServlet galleryGuiUploadServlet;

	@Inject
	private GalleryGuiImageListServlet galleryGuiListServlet;

	@Inject
	private GalleryGuiImageServlet galleryGuiImageServlet;

	@Inject
	private GalleryGuiImageDeleteServlet galleryGuiDeleteServlet;

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
		result.add(new ServletInfo(galleryGuiServlet, GalleryGuiConstants.URL_HOME));
		result.add(new ServletInfo(galleryGuiUploadServlet, GalleryGuiConstants.URL_IMAGE_UPLOAD));
		result.add(new ServletInfo(galleryGuiListServlet, GalleryGuiConstants.URL_IMAGE_LIST));
		result.add(new ServletInfo(galleryGuiImageServlet, GalleryGuiConstants.URL_IMAGE));
		result.add(new ServletInfo(galleryGuiDeleteServlet, GalleryGuiConstants.URL_DELETE));
		result.add(new ServletInfo(galleryGuiCreateServlet, GalleryGuiConstants.URL_CREATE));
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
