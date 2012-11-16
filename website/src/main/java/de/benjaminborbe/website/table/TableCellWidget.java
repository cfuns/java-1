package de.benjaminborbe.website.table;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.form.HasClass;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.TagWidget;

public class TableCellWidget extends CompositeWidget implements Widget, HasClass<TableCellWidget> {

	private Widget content;

	private final Set<String> classes = new HashSet<String>();

	public TableCellWidget() {
	}

	public TableCellWidget(final Widget content) {
		this.content = content;
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
		final TagWidget tag = new TagWidget("td", content);
		if (classes.size() > 0) {
			tag.addAttribute("class", StringUtils.join(classes, " "));
		}
		return tag;
	}
}
