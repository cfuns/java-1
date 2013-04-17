package de.benjaminborbe.website.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.form.HasClass;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.SingleTagWidget;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ImageWidget extends CompositeWidget implements HasClass<ImageWidget> {

	private final Set<String> classes = new HashSet<>();

	private String path;

	private long height;

	private long width;

	private String alt;

	public ImageWidget(final String path) {
		addSrc(path);
	}

	public ImageWidget(final String path, final int width, final int height) {
		this(path);
		addWidth(width);
		addHeight(height);
	}

	public ImageWidget addSrc(final String path) {
		this.path = path;
		return this;
	}

	public ImageWidget addWidth(final long width) {
		this.width = width;
		return this;
	}

	public ImageWidget addHeight(final long height) {
		this.height = height;
		return this;
	}

	public ImageWidget addAlt(final String alt) {
		this.alt = alt;
		return this;
	}

	@Override
	public ImageWidget addClass(final String clazz) {
		classes.add(clazz);
		return this;
	}

	@Override
	public ImageWidget removeClass(final String clazz) {
		classes.remove(clazz);
		return this;
	}

	@Override
	public Collection<String> getClasses() {
		return classes;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final SingleTagWidget tag = new SingleTagWidget("img");
		if (classes.size() > 0) {
			tag.addAttribute("class", StringUtils.join(classes, " "));
		}
		if (path != null) {
			tag.addAttribute("src", path);
		}
		if (height > 0) {
			tag.addAttribute("height", height);
		}
		if (width > 0) {
			tag.addAttribute("width", width);
		}
		if (alt != null) {
			tag.addAttribute("alt", alt);
		}
		return tag;
	}
}
