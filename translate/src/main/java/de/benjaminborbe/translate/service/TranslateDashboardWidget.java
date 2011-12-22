package de.benjaminborbe.translate.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardWidget;

@Singleton
public class TranslateDashboardWidget implements DashboardWidget {

	@Inject
	public TranslateDashboardWidget(final Logger logger) {
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<form method=\"GET\" action=\"http://dict.leo.org/ende\">");
		out.println("<input type=\"text\" name=\"search\">");
		out.println("<input type=\"submit\" value=\"translate\">");
		out.println("</from>");
	}

	@Override
	public String getTitle() {
		return "TranslateWidget";
	}
}
