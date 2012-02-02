package de.benjaminborbe.slash.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.website.servlet.WebsiteTextServlet;

@Singleton
public class SlashGuiRobotsTxtServlet extends WebsiteTextServlet {

	private static final long serialVersionUID = -7647639127591841698L;

	@Inject
	public SlashGuiRobotsTxtServlet(final Logger logger) {
		super(logger);
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("User-agent: *");
		out.println("Disallow: /css/");
		out.println("Disallow: /images/");
	}
}
