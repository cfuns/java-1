package de.benjaminborbe.website.link;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.TagWidget;

public class LinkSkypeWidget extends TagWidget implements Widget {

	private static final String TAG = "a";

	private final String skypeUsername;

	public LinkSkypeWidget(final String skypeUsername) {
		this(skypeUsername, skypeUsername);
	}

	public LinkSkypeWidget(final String skypeUsername, final String content) {
		super(TAG, content);
		this.skypeUsername = skypeUsername;
	}

	public LinkSkypeWidget(final String skypeUsername, final Widget content) {
		super(TAG, content);
		this.skypeUsername = skypeUsername;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		addAttribute("href", "skype:" + skypeUsername);
		super.render(request, response, context);
	}
}
