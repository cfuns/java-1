package de.benjaminborbe.website.widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.util.TagWidget;

public class TooltipWidget extends CompositeWidget {

	private final Widget content;

	private String tooltip;

	public TooltipWidget(final Widget content) {
		this.content = content;
	}

	public TooltipWidget(final String content) {
		this(new StringWidget(content));
	}

	public TooltipWidget addTooltip(final String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final TagWidget tag = new TagWidget("span", content);
		if (tooltip != null) {
			tag.addAttribute("onmouseover", "tooltipOver(event,'" + tooltip + "')");
			tag.addAttribute("onmouseout", "tooltipOut(event,'" + tooltip + "')");
			tag.addAttribute("onmousemove", "tooltipMove(event,'" + tooltip + "')");
		}
		return tag;
	}
}
