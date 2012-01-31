package de.benjaminborbe.website.table;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class Table implements Widget {

	private final List<TableRow> rows = new ArrayList<TableRow>();

	private TableHead head;

	public Table addRow(final TableRow row) {
		rows.add(row);
		return this;
	}

	public Table setRow(final TableHead head) {
		this.head = head;
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<table>");
		if (head != null) {
			head.render(request, response, context);
		}
		for (final TableRow row : rows) {
			row.render(request, response, context);
		}
		out.println("</table>");
	}
}
