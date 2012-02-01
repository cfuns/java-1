package de.benjaminborbe.mail.gui.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;

@Singleton
public class MailGuiDashboardWidget implements DashboardContentWidget {

	private final Logger logger;

	@Inject
	public MailGuiDashboardWidget(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("render");
		final PrintWriter out = response.getWriter();
		out.println("0 new mails");
	}

	@Override
	public String getTitle() {
		return "Mail";
	}

	@Override
	public long getPriority() {
		return 1;
	}

}
