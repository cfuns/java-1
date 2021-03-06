package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Calendar;

public class PortfolioGuiLinkFactory {

	private final CalendarUtil calendarUtil;

	@Inject
	public PortfolioGuiLinkFactory(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	public String imageLink(final HttpServletRequest request, final GalleryImageIdentifier galleryImageIdentifier) throws UnsupportedEncodingException {
		return request.getContextPath() + "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_IMAGE + "/" + String.valueOf(galleryImageIdentifier);
	}

	public Widget createContact(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT, "contacts");
	}

	public Widget createLinks(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_LINKS, "links");
	}

	public Widget createGallery(final HttpServletRequest request, final GalleryCollection gallery) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_GALLERY + "/" + String.valueOf(gallery.getId()), gallery.getName());
	}

	public Widget createCopyright(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT, "© " + getYear() + " by Benjamin Borbe");
	}

	private int getYear() {
		final Calendar c = calendarUtil.now();
		return c.get(Calendar.YEAR);
	}

	public String createGalleryUrl(final HttpServletRequest request, final GalleryCollection galleryCollection) {
		return request.getContextPath() + "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_GALLERY + "/" + String.valueOf(galleryCollection.getId());
	}
}
