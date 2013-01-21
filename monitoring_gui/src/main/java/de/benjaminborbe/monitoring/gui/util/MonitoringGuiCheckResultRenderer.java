package de.benjaminborbe.monitoring.gui.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringNodeResult;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.tools.url.UrlUtil;

public class MonitoringGuiCheckResultRenderer implements Widget {

	private final MonitoringNodeResult checkResult;

	private final UrlUtil urlUtil;

	public MonitoringGuiCheckResultRenderer(final MonitoringNodeResult checkResult, final UrlUtil urlUtil) {
		this.checkResult = checkResult;
		this.urlUtil = urlUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		if (checkResult.isSuccessful()) {
			out.print("[<span class=\"checkResultOk\">OK</span>] ");
		}
		else {
			out.print("[<span class=\"checkResultFail\">FAIL</span>] ");
		}

		out.print(checkResult.getName());
		out.print(" ");
		if (!checkResult.isSuccessful()) {
			out.print("(");
			out.print(checkResult.getMessage());
			out.print(")");
			out.print(" ");
		}
		if (checkResult.getUrl() != null) {
			out.println("<a href=\"" + checkResult.getUrl().toExternalForm() + "\" target=\"" + Target.BLANK + "\">");
			out.println("goto");
			out.println("</a>");
			out.print(" ");
		}
		out.println("<a href=\"" + request.getContextPath() + "/monitoring/silent?check=" + urlUtil.encode(checkResult.getId().getId()) + "\">silent</a>");
	}
}
