package de.benjaminborbe.search.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SearchGuiOsdServlet extends HttpServlet {

	private static final long serialVersionUID = 2841304297821086170L;

	@Inject
	private Logger logger;

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.trace("osd.xml");
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		final PrintWriter out = response.getWriter();

		final String host = request.getServerName();
		final int port = request.getServerPort();
		final String contextPath = request.getContextPath();
		final String protocol = "http";
		final String baseUrl = protocol + "://" + host + (port != 80 ? ":" + port : "") + contextPath;
		final String shortName = "BB Search";
		final String description = "BB Search";
		final String longName = "BB Search";
		final String email = "bborbe@rocketnews.de";

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<OpenSearchDescription xmlns=\"http://a9.com/-/spec/opensearch/1.1/\">");
		out.println("<ShortName>" + shortName + "</ShortName>");
		out.println("<LongName>" + longName + "</LongName>");
		out.println("<Description>" + description + "</Description>");
		out.println("<Contact>" + email + "</Contact>");
		out.println("<Url type=\"application/x-suggestions+json\" method=\"get\" rel=\"suggestions\" template=\"" + baseUrl + "/search/suggest?q={searchTerms}\" />");
		out.println("<Url type=\"text/html\" method=\"get\" template=\"" + baseUrl + "/search?q={searchTerms}\" />");
		out.println("<Image width=\"16\" height=\"16\">data:image/x-icon;base64,R0lGODlhEAAQAOeSACEMFB8OFiUMFCsOGDgKGDEQGCoVHBgdIzEUHUAOGDUWHDUWHTEYITEaIRgkOjoYIUIWH1AQGjEeJ0YWHVYQGkQYIkoWIEYYIToeJT4dKj4eJU4YIRgtRlYWIUocJ0IkLX4KFmsUH20UIWkYIWIcJXEWIYQQGD4vOjE6RoQYI0Y0OEI1Qko1PFgvOm0nL041PidEZmQvNkI+REJCSGQzPUpAQlY+RowxOnU7RHBCSpA1PnhASGFLWHlCSmROWHVJUGZRWG9OUohGTHlOVGRXXWVXX3NSWohKTmZYYGFcXGtaXmZcamNjZ4xYYHBmaHhjY3Bma31jaYxeYmhteG1tclZ0kHVtc4Bxd5tnb45vc357gW2EmJB4gJB5f4N/hH6BiYx8gGSKpG+ImJCAiJSAhKCAhpGJj6CFipCQkISWnnOcsZKSlI6UoKKNkq6LjpyUlK+MkJyUnLaVmqWeoamhpZ6ptamnrampq5qvuKGvt7GprbWosaa0vrOyt8KttaS6ytSrr721vcK2vri7w9q1t8a/wsq/xMbCxszCxs7GzsXU2tLS0tjS09jY2NTa2+Lc3N7i4uLr6////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEKAP8ALAAAAAAQABAAAAjKAP8JZESnjUCBWg4q9EFjhxs5/5TYeBJD4UEgQnhgiUIAx5EeNywKrJBjjKMRhB4hIiOykAUSXRCIALQFRRaRcwz8ADNgg44hIVIMiiSwjxcoGZbwKbKAQokWRtg0+meHyBkqX6oo+qfhgoc4LqbUESlwz4QOJhQk8ENW4JsCEEC84NJWoB4BARo0qSsQCQAGEaTUTeSEhQQMH8wIgmTxSpIaVpg8gLAijRg1eQSiOWEIzp0ZBxxwgBEGz5+Da2SoCFLm36JAh0QGBAA7</Image>");
		out.println("<Query role=\"example\" searchTerms=\"Search\" />");
		out.println("<Developer>Tester</Developer>");
		out.println("<Attribution>Testing</Attribution>");
		out.println("<SyndicationRight>open</SyndicationRight>");
		out.println("<AdultContent>false</AdultContent>");
		out.println("<Language>en-us</Language>");
		out.println("<OutputEncoding>UTF-8</OutputEncoding>");
		out.println("<InputEncoding>UTF-8</InputEncoding>");
		out.println("</OpenSearchDescription>");
	}
}
