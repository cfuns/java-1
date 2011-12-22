package de.benjaminborbe.index.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RobotsTxtServlet extends HttpServlet {

	private static final long serialVersionUID = -7647639127591841698L;

	@Inject
	private Logger logger;

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("service");
		response.setContentType("text/plain");
		final PrintWriter out = response.getWriter();
		out.println("User-agent: *");
		out.println("Disallow: /css/");
		out.println("Disallow: /images/");
	}
}