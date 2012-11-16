package de.benjaminborbe.website.table;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

public class TableRowWidget extends CompositeWidget implements Widget {

	private final List<TableCellWidget> cells = new ArrayList<TableCellWidget>();

	public TableRowWidget addCell(final TableCellWidget cell) {
		cells.add(cell);
		return this;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {

		final ListWidget list = new ListWidget();
		for (final TableCellWidget cell : cells) {
			list.add(cell);
		}
		final TagWidget tagWidget = new TagWidget("tr", list);
		return tagWidget;
	}
}
