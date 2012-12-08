package de.benjaminborbe.website.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.form.HasClass;
import de.benjaminborbe.website.form.HasId;

public class UlWidget extends CompositeWidget implements HasClass<UlWidget>, HasId<UlWidget> {

	private final Set<String> classes = new HashSet<String>();

	private final ListWidget list = new ListWidget();

	private String id;

	public UlWidget add(final LiWidget widget) {
		list.add(widget);
		return this;
	}

	public UlWidget add(final String content) {
		return add(new LiWidget(content));
	}

	public UlWidget add(final Widget widget) {
		return add(new LiWidget(widget));
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final TagWidget tag = new TagWidget("ul", list);
		if (classes.size() > 0) {
			tag.addAttribute("class", StringUtils.join(classes, " "));
		}
		if (id != null) {
			tag.addAttribute("id", id);
		}
		return tag;
	}

	@Override
	public UlWidget addClass(final String clazz) {
		classes.add(clazz);
		return this;
	}

	@Override
	public UlWidget removeClass(final String clazz) {
		classes.remove(clazz);
		return this;
	}

	@Override
	public Collection<String> getClasses() {
		return classes;
	}

	@Override
	public UlWidget addId(final String id) {
		this.id = id;
		return this;
	}

	@Override
	public String getId() {
		return null;
	}

}
