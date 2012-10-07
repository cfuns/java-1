package de.benjaminborbe.website.widget;

import de.benjaminborbe.website.util.SingleTagWidget;

public class ImageWidget extends SingleTagWidget {

	private static final String TAG = "img";

	public ImageWidget() {
		super(TAG);
	}

	public ImageWidget(final String path) {
		super(TAG);
		addSrc(path);
	}

	public ImageWidget addSrc(final String path) {
		addAttribute("src", path);
		return this;
	}

	public ImageWidget addWidth(final long width) {
		addAttribute("width", String.valueOf(width));
		return this;
	}

	public ImageWidget addHeight(final long height) {
		addAttribute("height", String.valueOf(height));
		return this;
	}

	public ImageWidget addAlt(final String alt) {
		addAttribute("alt", alt);
		return this;
	}

}
