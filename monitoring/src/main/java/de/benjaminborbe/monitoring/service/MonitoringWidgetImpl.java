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
import de.benjaminborbe.monitoring.api.MonitoringWidget;
import de.benjaminborbe.monitoring.check.CheckResult;
import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.RootNode;
import de.benjaminborbe.tools.io.FlushPrintWriter;

@Singleton
public class MonitoringWidgetImpl implements MonitoringWidget {

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
	public MonitoringWidgetImpl(final Logger logger, final RootNode rootNode, final NodeChecker nodeChecker) {
		this.logger = logger;
		this.rootNode = rootNode;
		this.nodeChecker = nodeChecker;
	}

	protected void printCheckWithRootNode(final FlushPrintWriter out) {
		final List<CheckResult> checkResults = new ArrayList<CheckResult>(nodeChecker.checkNode(rootNode));
		Collections.sort(checkResults, new CheckResultComparator());
		out.println("<ul>");
		for (final CheckResult checkResult : checkResults) {
			logger.debug(checkResult.toString());
			out.println("<li>");
			out.println(checkResult.toString());
			out.println("</li>");
		}
		out.println("</ul>");
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("render");
		final FlushPrintWriter out = new FlushPrintWriter(response.getWriter());
		out.println("monitoring checks started");
		printCheckWithRootNode(out);
		out.println("monitoring checks finished");
	}

}
