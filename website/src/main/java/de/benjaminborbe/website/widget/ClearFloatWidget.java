package de.benjaminborbe.website.widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.TagWidget;

public class ClearFloatWidget extends CompositeWidget {

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final TagWidget widget = new TagWidget("span");
		widget.addAttribute("style", "clear: both");
		return widget;
	}

}
