package de.benjaminborbe.portfolio.gui.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.portfolio.gui.util.GalleryComparator;
import de.benjaminborbe.website.util.UlWidget;

public class BottomNaviWidget implements Widget {

	private final GalleryService galleryService;

	private final GalleryComparator galleryComparator;

	@Inject
	public BottomNaviWidget(final GalleryService galleryService, final GalleryComparator galleryComparator) {
		this.galleryService = galleryService;
		this.galleryComparator = galleryComparator;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final UlWidget ul = new UlWidget();
		ul.addAttribute("class", "navi");
		ul.add(new LinksLinkWidget());
		ul.add(new ContactLinkWidget());
		ul.render(request, response, context);
	}

	protected List<GalleryCollection> getGalleries(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		final GalleryGroupIdentifier gi = galleryService.getGroupByName(sessionIdentifier, PortfolioGuiConstants.GROUP_NAME_NAVI_BOTTON);
		if (gi == null) {
			return new ArrayList<GalleryCollection>();
		}
		final List<GalleryCollection> galleries = new ArrayList<GalleryCollection>(galleryService.getCollectionsWithGroup(sessionIdentifier, gi));
		Collections.sort(galleries, galleryComparator);
		return galleries;
	}
}
