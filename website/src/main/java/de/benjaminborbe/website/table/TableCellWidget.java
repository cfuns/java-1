package de.benjaminborbe.website.table;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.form.HasAttribute;
import de.benjaminborbe.website.form.HasClass;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.util.TagWidget;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TableCellWidget extends CompositeWidget implements Widget, HasClass<TableCellWidget>, HasAttribute<TableCellWidget> {

	private Widget content;

	private final Set<String> classes = new HashSet<>();

	private final Map<String, String> attributes = new HashMap<>();

	public TableCellWidget() {
	}

	public TableCellWidget(final Widget content) {
		this.content = content;
	}

	public TableCellWidget(final String content) {
		this(new StringWidget(content));
	}

	public TableCellWidget setContent(final Widget content) {
		this.content = content;
		return this;
	}

	@Override
	public TableCellWidget addClass(final String clazz) {
		classes.add(clazz);
		return this;
	}

	@Override
	public TableCellWidget removeClass(final String clazz) {
		classes.remove(clazz);
		return this;
	}

	@Override
	public Collection<String> getClasses() {
		return classes;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final TagWidget tag = new TagWidget(getTag(), content);
		for (final Entry<String, String> e : attributes.entrySet()) {
			tag.addAttribute(e.getKey(), e.getValue());
		}
		if (classes.size() > 0) {
			tag.addAttribute("class", StringUtils.join(classes, " "));
		}
		return tag;
	}

	protected String getTag() {
		return "td";
	}

	@Override
	public TableCellWidget addAttribute(final String name, final String value) {
		attributes.put(name, value);
		return this;
	}
}
