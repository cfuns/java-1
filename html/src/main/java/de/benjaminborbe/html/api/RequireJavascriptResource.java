package de.benjaminborbe.html.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequireJavascriptResource {

	List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response);
}
