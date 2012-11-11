package de.benjaminborbe.portfolio.gui.widget;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class PortfolioLinkFactory {

	@Inject
	public PortfolioLinkFactory() {
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
