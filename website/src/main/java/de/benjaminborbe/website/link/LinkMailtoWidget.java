package de.benjaminborbe.website.link;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.TagWidget;

public class LinkMailtoWidget extends TagWidget implements Widget {

	private static final String TAG = "a";

	private final String email;

	public LinkMailtoWidget(final String email) {
		this(email, email);
	}

	public LinkMailtoWidget(final String email, final String content) {
		super(TAG, content);
		this.email = email;
	}

	public LinkMailtoWidget(final String email, final Widget content) {
		super(TAG, content);
		this.email = email;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		addAttribute("href", "mailto:" + email);
		super.render(request, response, context);
	}
}
