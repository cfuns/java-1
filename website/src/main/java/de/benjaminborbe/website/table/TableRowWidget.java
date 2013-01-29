package de.benjaminborbe.website.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.form.HasClass;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

public class TableRowWidget extends CompositeWidget implements Widget, HasClass<TableRowWidget> {

	private final Set<String> classes = new HashSet<String>();

	private final List<TableCellWidget> cells = new ArrayList<TableCellWidget>();

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {

		final ListWidget list = new ListWidget();
		for (final TableCellWidget cell : cells) {
			list.add(cell);
		}
		final TagWidget tagWidget = new TagWidget("tr", list);
		if (classes.size() > 0) {
			tagWidget.addAttribute("class", StringUtils.join(classes, " "));
		}
		return tagWidget;
	}

	public TableRowWidget addCell(final TableCellWidget cell) {
		cells.add(cell);
		return this;
	}

	public TableRowWidget addCell(final String string) {
		addCell(new TableCellWidget(string));
		return this;
	}

	public TableRowWidget addCell(final Widget widget) {
		addCell(new TableCellWidget(widget));
		return this;
	}

	@Override
	public TableRowWidget addClass(final String clazz) {
		classes.add(clazz);
		return this;
	}

	@Override
	public TableRowWidget removeClass(final String clazz) {
		classes.remove(clazz);
		return this;
	}

	@Override
	public Collection<String> getClasses() {
		return classes;
	}
}
