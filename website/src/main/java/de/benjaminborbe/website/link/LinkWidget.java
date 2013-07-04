package de.benjaminborbe.website.link;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.util.Target;

import java.net.URL;

public class LinkWidget extends TagWidget {

	public LinkWidget(final String url, final Widget contentWidget) {
		super("a", contentWidget);
		addAttribute("href", url);
	}

	public LinkWidget(final String url, final String content) {
		this(url, new StringWidget(content));
	}

	public LinkWidget(final URL url, final Widget contentWidget) {
		this(url.toExternalForm(), contentWidget);
	}

	public LinkWidget(final URL url, final String content) {
		this(url, new StringWidget(content));
	}

	public LinkWidget addTarget(final Target target) {
		addAttribute("target", String.valueOf(target));
		return this;
	}

	public LinkWidget addConfirm(final String message) {
		addAttribute("onclick", "if (confirm('" + message + "')) {return true;} else { return false; }");
		return this;
	}

}
