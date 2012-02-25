package de.benjaminborbe.translate.gui.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;

@Singleton
public class TranslateGuiDashboardWidget implements DashboardContentWidget {

	private static final Target target = Target.BLANK;

	private final Logger logger;

	@Inject
	public TranslateGuiDashboardWidget(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		logger.trace("render");
		final String action = "http://dict.leo.org/ende";
		final FormWidget formWidget = new FormWidget(action).addMethod(FormMethod.GET).addTarget(target);
		formWidget.addFormInputWidget(new FormInputTextWidget("search").addPlaceholder("translate ..."));
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
