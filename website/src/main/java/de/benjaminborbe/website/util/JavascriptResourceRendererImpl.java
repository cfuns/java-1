package de.benjaminborbe.website.util;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;

@Singleton
public class JavascriptResourceRendererImpl implements JavascriptResourceRenderer {

	@Inject
	public JavascriptResourceRendererImpl() {
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context, final Collection<JavascriptResource> javascriptResources)
			throws IOException {
		final HtmlListWidget widgets = new HtmlListWidget();
		for (final JavascriptResource javascriptResource : javascriptResources) {
			widgets.add("<script type=\"text/javascript\" src=\"" + javascriptResource.getUrl() + "\"></script>\n");
		}
		widgets.render(request, response, context);
	}
}
