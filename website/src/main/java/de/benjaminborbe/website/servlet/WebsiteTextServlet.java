package de.benjaminborbe.website.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

@Singleton
public abstract class WebsiteTextServlet extends HttpServlet {

	private static final long serialVersionUID = 5575454149618155404L;

	private final Logger logger;

	@Inject
	public WebsiteTextServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.trace("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		final HttpContext context = new HttpContext();
		final Widget widget = createContentWidget(request, response, context);
		widget.render(request, response, context);
	}

	protected abstract Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException;
}
