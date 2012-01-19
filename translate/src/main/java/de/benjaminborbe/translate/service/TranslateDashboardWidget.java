package de.benjaminborbe.translate.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.website.util.FormInputSubmitWidget;
import de.benjaminborbe.website.util.FormInputTextWidget;
import de.benjaminborbe.website.util.FormWidget;

@Singleton
public class TranslateDashboardWidget implements DashboardContentWidget {

	private static final Target target = Target.BLANK;

	private final Logger logger;

	@Inject
	public TranslateDashboardWidget(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("render");
		final String action = "http://dict.leo.org/ende";
		final FormWidget formWidget = new FormWidget(action).addMethod("GET").addTarget(target);
		formWidget.addFormInputWidget(new FormInputTextWidget("search"));
		formWidget.addFormInputWidget(new FormInputSubmitWidget("translate"));
		formWidget.render(request, response, context);
	}

	@Override
	public String getTitle() {
		return "TranslateWidget";
	}

	@Override
	public long getPriority() {
		return 998;
	}
}
