package de.benjaminborbe.website;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.website.util.SingleTagWidget;

public class ImageWidget extends SingleTagWidget {

	private static final String TAG = "img";

	private String path;

	public ImageWidget() {
		super(TAG);
	}

	public ImageWidget(final String path) {
		super(TAG);
		this.path = path;
	}

	public ImageWidget addPath(final String path) {
		this.path = path;
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		addAttribute("src", path);
		super.render(request, response, context);
	}

}
