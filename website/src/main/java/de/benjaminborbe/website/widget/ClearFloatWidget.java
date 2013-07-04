package de.benjaminborbe.website.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.SingleTagWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClearFloatWidget extends CompositeWidget {

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final SingleTagWidget widget = new SingleTagWidget("br");
		widget.addAttribute("style", "clear: both");
		return widget;
	}

}
