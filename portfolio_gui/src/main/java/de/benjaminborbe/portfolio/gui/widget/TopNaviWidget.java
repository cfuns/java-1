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

import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.util.GalleryComparator;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TopNaviWidget implements Widget {

	private final GalleryService galleryService;

	private final UrlUtil urlUtil;

	private final Logger logger;

	@Inject
	public TopNaviWidget(final Logger logger, final GalleryService galleryService, final UrlUtil urlUtil) {
		this.logger = logger;
		this.galleryService = galleryService;
		this.urlUtil = urlUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final UlWidget ul = new UlWidget();
			final List<Gallery> galleries = getGalleries();
			logger.debug("found " + galleries.size() + " galleries");
			for (final Gallery gallery : galleries) {
				ul.add(new GalleryLinkWidget(gallery, urlUtil));
			}
			ul.render(request, response, context);
		}
		catch (final GalleryServiceException e) {
			logger.debug(e.getClass().getName(), e);
			// nop
		}
	}

	protected List<Gallery> getGalleries() throws GalleryServiceException {
		final List<Gallery> galleries = new ArrayList<Gallery>(galleryService.getGalleries());
		Collections.sort(galleries, new GalleryComparator());
		return galleries;
	}

}
