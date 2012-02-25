package de.benjaminborbe.website.table;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class TableRow implements Widget {

	private final List<TableCell> cells = new ArrayList<TableCell>();

	public TableRow addCell(final TableCell cell) {
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		final PrintWriter out = response.getWriter();
		out.println("<" + getTagName() + ">");
		for (final TableCell cell : cells) {
			cell.render(request, response, context);
		}
		out.println("</" + getTagName() + ">");
	}

	protected String getTagName() {
		return "tr";
	}
}
