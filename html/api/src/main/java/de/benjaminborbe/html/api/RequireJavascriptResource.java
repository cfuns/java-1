package de.benjaminborbe.html.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RequireJavascriptResource {

	List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response);
}
