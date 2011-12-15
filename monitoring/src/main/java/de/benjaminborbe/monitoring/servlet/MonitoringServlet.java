package de.benjaminborbe.monitoring.servlet;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.check.CheckResult;
import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.RootNode;
import de.benjaminborbe.tools.io.FlushPrintWriter;

@Singleton
public class MonitoringServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final RootNode rootNode;

	private final NodeChecker nodeChecker;

	@Inject
	public MonitoringServlet(final Logger logger, final RootNode rootNode, final NodeChecker nodeChecker) {
		this.logger = logger;
		this.rootNode = rootNode;
		this.nodeChecker = nodeChecker;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/plain");
		final FlushPrintWriter out = new FlushPrintWriter(response.getWriter());
		out.println("monitoring checks started");
		printCheckWithRootNode(out);
		out.println("monitoring checks finished");
	}

	protected void printCheckWithRootNode(final FlushPrintWriter out) {
		final Collection<CheckResult> checkResults = nodeChecker.checkNode(rootNode);
		for (final CheckResult checkResult : checkResults) {
			logger.debug(checkResult.toString());
			out.println(checkResult.toString());
		}
	}

}
