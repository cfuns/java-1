package de.benjaminborbe.dashboard.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequireCssResource {

	List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response);
}
