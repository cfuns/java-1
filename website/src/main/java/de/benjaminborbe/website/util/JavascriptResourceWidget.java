package de.benjaminborbe.website.util;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;

public class JavascriptResourceWidget implements Widget {

	private final Collection<JavascriptResource> javascriptResources;

	@Inject
	public JavascriptResourceWidget(final Collection<JavascriptResource> javascriptResources) {
		this.javascriptResources = javascriptResources;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		final HtmlListWidget widgets = new HtmlListWidget();
		for (final JavascriptResource javascriptResource : javascriptResources) {
			widgets.add("<script type=\"text/javascript\" src=\"" + javascriptResource.getUrl() + "\"></script>\n");
		}
		widgets.render(request, response, context);
	}
}
