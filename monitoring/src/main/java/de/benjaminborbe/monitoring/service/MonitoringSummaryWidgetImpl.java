package de.benjaminborbe.monitoring.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.monitoring.api.MonitoringSummaryWidget;
import de.benjaminborbe.monitoring.check.CheckResult;
import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.RootNode;
import de.benjaminborbe.tools.io.FlushPrintWriter;

@Singleton
public class MonitoringSummaryWidgetImpl implements MonitoringSummaryWidget {

	private final class CheckResultComparator implements Comparator<CheckResult> {

		@Override
		public int compare(final CheckResult a, final CheckResult b) {
			return a.toString().compareTo(b.toString());
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final RootNode rootNode;

	private final NodeChecker nodeChecker;

	@Inject
	public MonitoringSummaryWidgetImpl(final Logger logger, final RootNode rootNode, final NodeChecker nodeChecker) {
		this.logger = logger;
		this.rootNode = rootNode;
		this.nodeChecker = nodeChecker;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("render");
		final FlushPrintWriter out = new FlushPrintWriter(response.getWriter());
		final List<CheckResult> checkResults = new ArrayList<CheckResult>(nodeChecker.checkNode(rootNode));
		Collections.sort(checkResults, new CheckResultComparator());
		final List<CheckResult> failCheckResults = new ArrayList<CheckResult>();
		for (final CheckResult checkResult : checkResults) {
			if (!checkResult.isSuccess()) {
				failCheckResults.add(checkResult);
			}
		}
		out.println(failCheckResults.size() + " checks failed! ");
		out.println("<a href=\"" + request.getContextPath() + "/monitoring\">Details</a>");
		out.println("<ul>");
		for (final CheckResult checkResult : failCheckResults) {
			logger.debug(checkResult.toString());
			out.println("<li>");
			out.println(checkResult.toString());
			out.println("</li>");
		}
		out.println("</ul>");
	}
}
