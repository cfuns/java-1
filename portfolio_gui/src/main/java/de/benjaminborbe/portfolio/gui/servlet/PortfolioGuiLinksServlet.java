package de.benjaminborbe.portfolio.gui.servlet;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.portfolio.gui.widget.PortfolioLayoutWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.WebsiteWidgetServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class PortfolioGuiLinksServlet extends WebsiteWidgetServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final PortfolioLayoutWidget portfolioWidget;

	private final GalleryService galleryService;

	private final AuthenticationService authenticationService;

	@Inject
	public PortfolioGuiLinksServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AuthenticationService authenticationService,
			final PortfolioLayoutWidget portfolioWidget,
			final GalleryService galleryService,
			final AuthorizationService authorizationService) {
		super(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService, authorizationService);
		this.portfolioWidget = portfolioWidget;
		this.authenticationService = authenticationService;
		this.galleryService = galleryService;
	}

	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget("Links"));
		final UlWidget links = new UlWidget();
		links.add(new LinkWidget(new URL("http://www.facebook.com/benjamin.borbe"), "Facebook"));
		links.add(new LinkWidget(new URL("http://twitter.com/bborbe"), "Twitter"));
		links.add(new LinkWidget(new URL("http://www.model-kartei.de/sedcard/fotograf/238596/"), "Model-Kartei-Profil"));
		links.add(new LinkWidget(new URL("http://www.stylished.de/fotografen-wiesbaden/fotograf-benjamin-borbe-124852.html"), "Stylished-Profil"));
		links.add(new LinkWidget(new URL("http://www.fotocommunity.de/pc/account/myprofile/1629106/profile/1"), "Fotocommunity"));
		links.add(new LinkWidget(new URL("http://www.flickr.com/photos/borbe/sets/"), "Flickr"));
		links.add(new LinkWidget(new URL("http://500px.com/ben/"), "500px"));
		links.add(new LinkWidget(new URL("http://www.behance.net/borbe"), "Behance"));
		links.add(new LinkWidget(new URL("http://www.rocketnews.de"), "Rocketnews"));
		links.add(new LinkWidget(new URL("http://www.harteslicht.de"), "Harteslicht"));
		widgets.add(links);
		return widgets;
	}

	@Override
	public Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final GalleryCollectionIdentifier galleryCollectionIdentifier = galleryService.getCollectionIdentifierByNameShared(sessionIdentifier,
					PortfolioGuiConstants.COLLECTION_NAME_LINKS);
			if (galleryCollectionIdentifier != null) {
				portfolioWidget.setGalleryEntries(galleryService.getEntriesShared(sessionIdentifier, galleryCollectionIdentifier));
			}
			portfolioWidget.addTitle("Links - Benjamin Borbe");
			portfolioWidget.addContent(createContentWidget(request, response, context));
			return portfolioWidget;
		}
		catch (final AuthenticationServiceException e) {
			return new ExceptionWidget(e);
		}
		catch (final GalleryServiceException e) {
			return new ExceptionWidget(e);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
