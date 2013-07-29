package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.form.HasClass;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SpanWidget extends TagWidget implements HasClass<SpanWidget> {

	private static final String TAG = "span";

	private final Set<String> classes = new HashSet<String>();

	public SpanWidget(final Widget contentWidget) {
		super(TAG, contentWidget);
	}

	public SpanWidget(final String content) {
		this(new StringWidget(content));
	}

	public SpanWidget() {
		super(TAG);
	}

	@Override
	public SpanWidget addClass(final String clazz) {
		classes.add(clazz);
		return this;
	}

	@Override
	public SpanWidget removeClass(final String clazz) {
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
