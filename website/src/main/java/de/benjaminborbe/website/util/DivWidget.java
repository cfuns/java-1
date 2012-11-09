package de.benjaminborbe.website.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.form.HasClass;

public class DivWidget extends TagWidget implements HasClass<DivWidget> {

	private static final String TAG = "div";

	private final Set<String> classes = new HashSet<String>();

	public DivWidget() {
		super(TAG);
	}

	public DivWidget(final Widget contentWidget) {
		super(TAG, contentWidget);
	}

	public DivWidget(final String content) {
		this(new StringWidget(content));
	}

	@Override
	public DivWidget addClass(final String clazz) {
		classes.add(clazz);
		return this;
	}

	@Override
	public DivWidget removeClass(final String clazz) {
		classes.remove(clazz);
		return this;
	}

	@Override
	public Collection<String> getClasses() {
		return classes;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		if (classes.size() > 0) {
			addAttribute("class", StringUtils.join(classes, " "));
		}
		super.render(request, response, context);
	}
}
