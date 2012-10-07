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
import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.portfolio.gui.util.GalleryComparator;
import de.benjaminborbe.portfolio.gui.widget.PortfolioWidget;
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

	private final PortfolioWidget portfolioWidget;

	private final GalleryService galleryService;

	@Inject
	public PortfolioGuiGalleryServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AuthenticationService authenticationService,
			final PortfolioWidget portfolioWidget,
			final GalleryService galleryService) {
		super(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService);
		this.portfolioWidget = portfolioWidget;
		this.galleryService = galleryService;
	}

	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final ListWidget widgets = new ListWidget();
			final List<GalleryImageIdentifier> images = getImages(request);
			for (final GalleryImageIdentifier image : images) {
				widgets.add(new ImageWidget(request.getContextPath() + "/gallery/image/content?image_id=" + image.getId()));
			}
			return widgets;
		}
		catch (final GalleryServiceException e) {
			return new ExceptionWidget(e);
		}
	}

	@Override
	public Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		portfolioWidget.addTitle("Portfolio - Gallery");
		portfolioWidget.addContent(createContentWidget(request, response, context));
		return portfolioWidget;
	}

	private List<GalleryImageIdentifier> getImages(final HttpServletRequest request) throws GalleryServiceException {
		final String galleryId = request.getParameter(PortfolioGuiConstants.PARAMETER_GALLERY_ID);
		if (galleryId != null) {
			final GalleryIdentifier galleryIdentifier = galleryService.createGalleryIdentifier(galleryId);
			return galleryService.getImageIdentifiers(galleryIdentifier);
		}
		else {
			final List<Gallery> galleries = new ArrayList<Gallery>(galleryService.getGalleries());
			Collections.sort(galleries, new GalleryComparator());
			if (galleries.size() == 0) {
				return new ArrayList<GalleryImageIdentifier>();
			}
			else {
				final Gallery gallery = galleries.get(0);
				return galleryService.getImageIdentifiers(gallery.getId());
			}
		}
	}
}
