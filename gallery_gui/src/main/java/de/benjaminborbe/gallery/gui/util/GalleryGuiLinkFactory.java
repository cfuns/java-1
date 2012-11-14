package de.benjaminborbe.gallery.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

@Singleton
public class GalleryGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public GalleryGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget createGallery(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GALLERY_CREATE, "create gallery");
	}

	public Widget imageList(final HttpServletRequest request, final Gallery gallery) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_IMAGE_LIST, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_GALLERY_ID, String.valueOf(gallery.getId())), gallery.getName());
	}

	public Widget deleteGallery(final HttpServletRequest request, final Gallery gallery) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GALLERY_DELETE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_GALLERY_ID, String.valueOf(gallery.getId())), "delete gallery");
	}
}
