package de.benjaminborbe.portfolio.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class PortfolioLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public PortfolioLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public String imageLink(final HttpServletRequest request, final GalleryImageIdentifier galleryImageIdentifier) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/gallery/image/content",
				new MapParameter().add(PortfolioGuiConstants.PARAMETER_IMAGE_ID, String.valueOf(galleryImageIdentifier)));
	}

	public Widget createContact(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT, "contacts");
	}

	public Widget createLinks(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_LINKS, "links");
	}

	public Widget createGallery(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT, "contacts");
	}

	public Widget createCopyright(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT, "© 2011 by Benjamin Borbe");
	}

}
