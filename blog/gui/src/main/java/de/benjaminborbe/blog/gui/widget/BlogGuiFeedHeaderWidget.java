package de.benjaminborbe.blog.gui.widget;

import de.benjaminborbe.blog.gui.BlogGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.util.SingleTagWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BlogGuiFeedHeaderWidget implements Widget {

	private final UrlUtil urlUtil;

	@Inject
	public BlogGuiFeedHeaderWidget(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final SingleTagWidget widget = new SingleTagWidget("link").addAttribute("type", BlogGuiConstants.ATOM_CONTENT_TYPE).addAttribute("title", BlogGuiConstants.ATOM_TITLE)
			.addAttribute("href", urlUtil.buildBaseUrl(request) + "/" + BlogGuiConstants.NAME + BlogGuiConstants.ATOM_URL);
		widget.render(request, response, context);
	}
}
