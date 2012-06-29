package de.benjaminborbe.slash.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.servlet.WebsiteTextServlet;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class SlashGuiRobotsTxtServlet extends WebsiteTextServlet {

	private static final long serialVersionUID = -7647639127591841698L;

	@Inject
	public SlashGuiRobotsTxtServlet(final Logger logger) {
		super(logger);
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add("User-agent: *");
		widgets.add("Disallow: /css/");
		widgets.add("Disallow: /images/");
		return widgets;
	}
}
