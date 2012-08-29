package de.benjaminborbe.geocaching.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.util.ResourceUtil;

@Singleton
public class GeocachingGuiCurrentLocationOnGoogleMapsServlet extends HttpServlet {

	private static final long serialVersionUID = -538341985801496273L;

	private final ResourceUtil resourceUtil;

	@Inject
	public GeocachingGuiCurrentLocationOnGoogleMapsServlet(final ResourceUtil resourceUtil) {
		this.resourceUtil = resourceUtil;
	}

	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resourceUtil.copyResourceToOutputStream("geo.html", resp.getOutputStream());
	}
}
