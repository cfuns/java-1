package de.benjaminborbe.cron.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.SchedulerException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.util.Quartz;

@Singleton
public class CronServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String PARAMETER_ACTION = "action";

	private static final String ACTION_START = "start";

	private static final String ACTION_STOP = "stop";

	private final Logger logger;

	private final Quartz quartz;

	@Inject
	public CronServlet(final Logger logger, final Quartz quartz) {
		this.logger = logger;
		this.quartz = quartz;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("service");
		response.setContentType("text/plain");
		final PrintWriter out = response.getWriter();
		final String action = request.getParameter(PARAMETER_ACTION);

		try {
			if (ACTION_START.equals(action)) {
				quartz.start();
				out.println("started");
			}
			else if (ACTION_STOP.equals(action)) {
				quartz.stop();
				out.println("stopped");
			}
			else {
				printUsage(out);
			}
		}
		catch (final SchedulerException e) {
		}
	}

	private void printUsage(final PrintWriter out) {
		out.println("parameter " + PARAMETER_ACTION + " missing or value != " + ACTION_START + " or " + ACTION_STOP);
	}
}
