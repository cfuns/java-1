package de.benjaminborbe.portfolio.gui.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TopNaviWidget implements Widget {

	private final GalleryService galleryService;

	private final UrlUtil urlUtil;

	@Inject
	public TopNaviWidget(final GalleryService galleryService, final UrlUtil urlUtil) {
		this.galleryService = galleryService;
		this.urlUtil = urlUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final UlWidget ul = new UlWidget();
			for (final Gallery gallery : getGalleries()) {
				ul.add(new GalleryLink(gallery, urlUtil));
			}
			ul.render(request, response, context);
		}
		catch (final GalleryServiceException e) {
			// nop
		}
	}

	protected List<Gallery> getGalleries() throws GalleryServiceException {
		final List<Gallery> galleries = new ArrayList<Gallery>(galleryService.getGalleries());
		Collections.sort(galleries, new GalleryComparator());
		return galleries;
	}

}
