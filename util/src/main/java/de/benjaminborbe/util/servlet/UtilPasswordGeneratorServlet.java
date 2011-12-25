package de.benjaminborbe.util.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.util.UtilPasswordCharacter;
import de.benjaminborbe.util.UtilPasswordGenerator;

@Singleton
public class UtilPasswordGeneratorServlet extends HttpServlet {

	private static final int PASSWORD_AMOUNT = 10;

	private static final long serialVersionUID = 2429004714466731564L;

	private static final UtilPasswordCharacter[] DEFAULT_CHARACTERS = { UtilPasswordCharacter.LOWER,
			UtilPasswordCharacter.UPPER, UtilPasswordCharacter.NUMBER, UtilPasswordCharacter.SPECIAL };

	private static final int DEFAULT_LENGHT = 8;

	private final Logger logger;

	private final UtilPasswordGenerator utilPasswordGenerator;

	@Inject
	public UtilPasswordGeneratorServlet(final Logger logger, final UtilPasswordGenerator utilPasswordGenerator) {
		this.logger = logger;
		this.utilPasswordGenerator = utilPasswordGenerator;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		printHtml(request, response);
	}

	protected void printHtml(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<html>");
		printHead(request, response);
		printBody(request, response);
		out.println("</html>");
	}

	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<body>");
		out.println("<h2>PasswordGenerator</h2>");
		out.println("<ul>");
		for (int i = 0; i < PASSWORD_AMOUNT; ++i) {
			out.println("<li>");
			out.println(utilPasswordGenerator.generatePassword(DEFAULT_LENGHT, DEFAULT_CHARACTERS));
			out.println("</li>");
		}
		out.println("</ul>");
		out.println("</body>");
	}

	protected void printHead(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<head>");
		out.println("</head>");
	}
}
