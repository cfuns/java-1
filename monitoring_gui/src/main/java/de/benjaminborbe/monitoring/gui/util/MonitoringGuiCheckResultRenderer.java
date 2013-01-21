package de.benjaminborbe.monitoring.gui.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.tools.url.UrlUtil;

public class MonitoringGuiCheckResultRenderer implements Widget {

	private final MonitoringNode checkResult;

	private final UrlUtil urlUtil;

	public MonitoringGuiCheckResultRenderer(final MonitoringNode checkResult, final UrlUtil urlUtil) {
		this.checkResult = checkResult;
		this.urlUtil = urlUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		if (checkResult.getResult() == null) {
			out.print("[<span class=\"checkResultUnknown\">???</span>] ");
		}
		else if (Boolean.TRUE.equals(checkResult.getResult())) {
			out.print("[<span class=\"checkResultOk\">OK</span>] ");
		}
		else {
			out.print("[<span class=\"checkResultFail\">FAIL</span>] ");
		}

		out.print(checkResult.getDescription());
		out.print(" (");
		out.print(checkResult.getName());
		out.print(") ");
		if (Boolean.FALSE.equals(checkResult.getResult())) {
			out.print("(");
			out.print(checkResult.getMessage() != null ? checkResult.getMessage() : "-");
			out.print(")");
			out.print(" ");
		}
		out.println("<a href=\"" + request.getContextPath() + "/monitoring/silent?check=" + urlUtil.encode(checkResult.getId().getId()) + "\">silent</a>");
	}
}
