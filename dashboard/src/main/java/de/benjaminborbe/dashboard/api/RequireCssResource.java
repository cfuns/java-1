package de.benjaminborbe.dashboard.api;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequireCssResource {

	Collection<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response);
}
