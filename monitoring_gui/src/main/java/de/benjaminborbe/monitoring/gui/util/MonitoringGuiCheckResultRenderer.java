package de.benjaminborbe.monitoring.gui.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HTML;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.tools.html.Target;

public class MonitoringGuiCheckResultRenderer implements HTML {

	private final CheckResult checkResult;

	public MonitoringGuiCheckResultRenderer(final CheckResult checkResult) {
		this.checkResult = checkResult;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		if (checkResult.isSuccess()) {
			out.print("[<span class=\"checkResultOk\">OK</span>] ");
		}
		else {
			out.print("[<span class=\"checkResultFail\">FAIL</span>] ");
		}

		if (checkResult.getUrl() != null) {
			out.println("<a href=\"" + checkResult.getUrl().toExternalForm() + "\" target=\"" + Target.BLANK + "\">");
		}
		out.print(checkResult.getName());
		out.print(" - ");
		out.print(checkResult.getMessage());
		if (checkResult.getUrl() != null) {
			out.println("</a>");
		}
	}

}
