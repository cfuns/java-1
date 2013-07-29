package de.benjaminborbe.microblog.gui.service;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.gui.MicroblogGuiConstants;
import de.benjaminborbe.website.util.Target;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MicroblogGuiDashboardWidget implements DashboardContentWidget, RequireCssResource {

	private static final String TITLE = "Microblog";

	private final Logger logger;

	private final MicroblogService microblogService;

	@Inject
	public MicroblogGuiDashboardWidget(final Logger logger, final MicroblogService microblogService) {
		this.logger = logger;
		this.microblogService = microblogService;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("render");
		final PrintWriter out = response.getWriter();
		String lastestRevision;
		try {
			lastestRevision = String.valueOf(microblogService.getLatestPostIdentifier());
		} catch (final MicroblogServiceException e) {
			lastestRevision = "-";
		}
		out.println("latest revision: " + lastestRevision + "<br/>");
		out.println("<a href=\"https://micro.rp.seibert-media.net/\" target=\"" + Target.BLANK + "\">Microblog</a>");
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public long getPriority() {
		return 1;
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		return new ArrayList<CssResource>();
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public String getName() {
		return MicroblogGuiConstants.NAME;
	}

}
