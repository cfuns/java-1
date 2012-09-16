package de.benjaminborbe.blog.gui.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.blog.gui.BlogGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.SingleTagWidget;

public class BlogGuiFeedHeaderWidget implements Widget {

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final SingleTagWidget widget = new SingleTagWidget("link").addAttribute("type", BlogGuiConstants.ATOM_CONTENT_TYPE).addAttribute("title", BlogGuiConstants.ATOM_TITLE)
				.addAttribute("href", request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/" + BlogGuiConstants.NAME + BlogGuiConstants.ATOM_URL);
		widget.render(request, response, context);
	}
}
