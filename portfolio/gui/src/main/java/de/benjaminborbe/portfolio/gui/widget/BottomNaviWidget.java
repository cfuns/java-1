package de.benjaminborbe.portfolio.gui.widget;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiGalleryCollectionComparator;
import de.benjaminborbe.website.util.UlWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BottomNaviWidget implements Widget {

	private final GalleryService galleryService;

	private final PortfolioGuiGalleryCollectionComparator galleryComparator;

	@Inject
	public BottomNaviWidget(final GalleryService galleryService, final PortfolioGuiGalleryCollectionComparator galleryComparator) {
		this.galleryService = galleryService;
		this.galleryComparator = galleryComparator;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final UlWidget ul = new UlWidget();
		ul.addClass("navi");
		ul.add(new LinksLinkWidget());
		ul.add(new ContactLinkWidget());
		ul.render(request, response, context);
	}

	protected List<GalleryCollection> getGalleries() throws GalleryServiceException {
		final GalleryGroupIdentifier gi = galleryService.getGroupByNameShared(PortfolioGuiConstants.GROUP_NAME_NAVI_BOTTON);
		if (gi == null) {
			return new ArrayList<GalleryCollection>();
		}
		final List<GalleryCollection> galleries = new ArrayList<GalleryCollection>(galleryService.getCollectionsWithGroupShared(gi));
		Collections.sort(galleries, galleryComparator);
		return galleries;
	}
}
