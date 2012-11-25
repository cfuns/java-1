package de.benjaminborbe.portfolio.gui.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiGalleryCollectionComparator;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiLinkFactory;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TopNaviWidget implements Widget {

	private final GalleryService galleryService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final PortfolioGuiGalleryCollectionComparator galleryComparator;

	private final PortfolioGuiLinkFactory portfolioLinkFactory;

	@Inject
	public TopNaviWidget(
			final Logger logger,
			final GalleryService galleryService,
			final AuthenticationService authenticationService,
			final PortfolioGuiGalleryCollectionComparator galleryComparator,
			final PortfolioGuiLinkFactory portfolioLinkFactory) {
		this.logger = logger;
		this.galleryService = galleryService;
		this.authenticationService = authenticationService;
		this.galleryComparator = galleryComparator;
		this.portfolioLinkFactory = portfolioLinkFactory;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final UlWidget ul = new UlWidget();
			ul.addAttribute("class", "navi");
			final List<GalleryCollection> galleries = getGalleries(sessionIdentifier);
			logger.debug("found " + galleries.size() + " galleries");
			for (final GalleryCollection gallery : galleries) {
				ul.add(portfolioLinkFactory.createGallery(request, gallery));
			}
			ul.render(request, response, context);
		}
		catch (final GalleryServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	protected List<GalleryCollection> getGalleries(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		final GalleryGroupIdentifier gi = galleryService.getGroupByNameShared(sessionIdentifier, PortfolioGuiConstants.GROUP_NAME_NAVI_TOP);
		if (gi == null) {
			return new ArrayList<GalleryCollection>();
		}
		final List<GalleryCollection> galleries = new ArrayList<GalleryCollection>(galleryService.getCollectionsWithGroupShared(sessionIdentifier, gi));
		Collections.sort(galleries, galleryComparator);
		return galleries;
	}
}
