package de.benjaminborbe.portfolio.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.portfolio.gui.util.GalleryComparator;
import de.benjaminborbe.portfolio.gui.util.PortfolioLinkFactory;
import de.benjaminborbe.website.servlet.RedirectUtil;

@Singleton
public class PortfolioGuiServlet extends HttpServlet {

	private static final long serialVersionUID = -6569405481753305393L;

	private final GalleryService galleryService;

	private final AuthenticationService authenticationService;

	private final GalleryComparator galleryComparator;

	private final PortfolioLinkFactory portfolioLinkFactory;

	private final RedirectUtil redirectUtil;

	private final Logger logger;

	@Inject
	public PortfolioGuiServlet(
			final Logger logger,
			final GalleryService galleryService,
			final GalleryComparator galleryComparator,
			final PortfolioLinkFactory portfolioLinkFactory,
			final AuthenticationService authenticationService,
			final RedirectUtil redirectUtil) {
		this.logger = logger;
		this.galleryService = galleryService;
		this.galleryComparator = galleryComparator;
		this.portfolioLinkFactory = portfolioLinkFactory;
		this.authenticationService = authenticationService;
		this.redirectUtil = redirectUtil;
	}

	private GalleryCollection getGalleryCollection(final HttpServletRequest request, final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		final List<GalleryCollection> galleries = new ArrayList<GalleryCollection>(galleryService.getCollections(sessionIdentifier));
		if (galleries.size() > 0) {
			Collections.sort(galleries, galleryComparator);
			return galleries.get(0);
		}
		else {
			return null;
		}
	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final GalleryCollection galleryCollection = getGalleryCollection(request, sessionIdentifier);
			final String target = portfolioLinkFactory.createGalleryUrl(request, galleryCollection);
			redirectUtil.sendRedirect(request, response, target);
		}
		catch (final AuthenticationServiceException e) {
			logger.error(e.getClass().getName(), e);
		}
		catch (final GalleryServiceException e) {
			logger.error(e.getClass().getName(), e);
		}
	}
}
