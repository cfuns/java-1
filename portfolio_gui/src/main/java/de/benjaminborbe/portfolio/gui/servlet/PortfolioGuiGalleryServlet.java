package de.benjaminborbe.portfolio.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.portfolio.gui.util.GalleryComparator;
import de.benjaminborbe.portfolio.gui.util.PortfolioLinkFactory;
import de.benjaminborbe.portfolio.gui.widget.PortfolioLayoutWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteWidgetServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ImageWidget;

@Singleton
public class PortfolioGuiGalleryServlet extends WebsiteWidgetServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final PortfolioLayoutWidget portfolioWidget;

	private final GalleryService galleryService;

	private final Logger logger;

	private final PortfolioLinkFactory portfolioLinkFactory;

	@Inject
	public PortfolioGuiGalleryServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AuthenticationService authenticationService,
			final PortfolioLayoutWidget portfolioWidget,
			final GalleryService galleryService,
			final PortfolioLinkFactory portfolioLinkFactory) {
		super(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService);
		this.portfolioWidget = portfolioWidget;
		this.logger = logger;
		this.galleryService = galleryService;
		this.portfolioLinkFactory = portfolioLinkFactory;
	}

	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final ListWidget widgets = new ListWidget();
			final List<GalleryEntryIdentifier> galleryEntryIdentifiers = getEntries(request);
			for (final GalleryEntryIdentifier galleryEntryIdentifier : galleryEntryIdentifiers) {
				final GalleryEntry entry = galleryService.getEntry(galleryEntryIdentifier);
				widgets.add(new ImageWidget(portfolioLinkFactory.imageLink(request, entry.getPreviewImageIdentifier())));
			}
			return widgets;
		}
		catch (final GalleryServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}

	@Override
	public Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		portfolioWidget.addTitle("Portfolio - Benjamin Borbe - Gallery");
		portfolioWidget.addContent(createContentWidget(request, response, context));
		return portfolioWidget;
	}

	private List<GalleryEntryIdentifier> getEntries(final HttpServletRequest request) throws GalleryServiceException {
		final String galleryId = request.getParameter(PortfolioGuiConstants.PARAMETER_GALLERY_ID);
		if (galleryId != null) {
			final GalleryCollectionIdentifier galleryIdentifier = galleryService.createCollectionIdentifier(galleryId);
			return galleryService.getEntryIdentifiers(galleryIdentifier);
		}
		else {
			final List<GalleryCollection> galleries = new ArrayList<GalleryCollection>(galleryService.getCollections());
			Collections.sort(galleries, new GalleryComparator());
			if (galleries.size() == 0) {
				return new ArrayList<GalleryEntryIdentifier>();
			}
			else {
				final GalleryCollection gallery = galleries.get(0);
				return galleryService.getEntryIdentifiers(gallery.getId());
			}
		}
	}

	@Override
	protected boolean isLoginRequired() {
		return false;
	}

}
