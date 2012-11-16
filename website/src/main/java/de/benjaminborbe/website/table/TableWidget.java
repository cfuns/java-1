package de.benjaminborbe.website.table;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.form.HasId;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

public class TableWidget extends CompositeWidget implements Widget, HasId<TableWidget> {

	private String id;

	private final List<TableRowWidget> rows = new ArrayList<TableRowWidget>();

	private TableHeadWidget head;

	private TableFootWidget foot;

	public TableWidget addRow(final TableRowWidget row) {
		rows.add(row);
		return this;
	}

	public TableWidget setHead(final TableHeadWidget head) {
		this.head = head;
		return this;
	}

	public TableWidget setFoot(final TableFootWidget foot) {
		this.foot = foot;
		return this;
	}

	@Override
	public TableWidget addId(final String id) {
		this.id = id;
		return this;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget parts = new ListWidget();
		if (head != null) {
			parts.add(new TagWidget("thead", head));
		}
		if (rows.size() > 0) {
			final ListWidget body = new ListWidget();
			for (final TableRowWidget row : rows) {
				body.add(row);
			}
			parts.add(new TagWidget("tbody", body));
		}
		if (foot != null) {
			parts.add(new TagWidget("tfoot", foot));
		}
		final TagWidget table = new TagWidget("table", parts);
		if (id != null) {
			table.addAttribute("id", id);
		}
		return table;
	}
}
