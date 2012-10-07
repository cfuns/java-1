package de.benjaminborbe.portfolio.gui.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class GalleryLink implements Widget {

	private final Gallery gallery;

	private final UrlUtil urlUtil;

	public GalleryLink(final Gallery gallery, final UrlUtil urlUtil) {
		this.gallery = gallery;
		this.urlUtil = urlUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final LinkRelativWidget link = new LinkRelativWidget(urlUtil, request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_HOME, new MapChain<String, String>().add(
				PortfolioGuiConstants.PARAMETER_GALLERY_ID, String.valueOf(gallery.getId())), gallery.getName());
		link.render(request, response, context);
	}

}
