package de.benjaminborbe.blog.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.blog.gui.BlogGuiConstants;
import de.benjaminborbe.blog.gui.atom.Feed;
import de.benjaminborbe.blog.gui.atom.FeedBean;
import de.benjaminborbe.blog.gui.widget.BlogGuiAtomWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.website.servlet.WebsiteWidgetServlet;

@Singleton
public class BlogGuiAtomServlet extends WebsiteWidgetServlet {

	private static final long serialVersionUID = -9150646730186060728L;

	@Inject
	public BlogGuiAtomServlet(final Logger logger, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil, final Provider<HttpContext> httpContextProvider) {
		super(logger, calendarUtil, timeZoneUtil, httpContextProvider);
	}

	@Override
	public Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final Feed feed = createFeed(request, response, context);
		return new BlogGuiAtomWidget(feed);
	}

	private Feed createFeed(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) {
		final AuthorBean author = new AuthorBean();
		author.setName("Benjamin Borbe");

		final FeedBean feed = new FeedBean();
		feed.setTitle(BlogGuiConstants.ATOM_TITLE);
		feed.setSubtitle("");
		feed.setLink(request.getRequestURL().toString());
		feed.setId(request.getRequestURL().toString());
		feed.setUpdated("2012-09-16T11:50:09+02:00");
		feed.setAuthor(author);
		return feed;
	}

	void printEntry(final PrintWriter out) {
		out.println("<entry>");
		out.println("<title>Title</title>");
		out.println("<link href=\"http://www.heise.de/newsticker/meldung/Studie-Mit-der-Bildung-steigt-die-Leidenschaft-fuer-Computerspiele-1708799.html/from/atom10\" />");
		out.println("<id>http://heise.de/-1708799</id>");
		out.println("<published>2012-09-16T11:50:00+02:00</published>");
		out.println("<updated>2012-09-16T11:50:09+02:00</updated>");
		out.println("<summary>Je höher der Bildungsabschluss, desto größer der Anteil von Spiele-Nutzern – diesen Zusammenhang will der IT-Branchenverband Bitkom durch eine von ihm beauftragte Umfrage belegen.</summary>");
		out.println("<content type=\"xhtml\">");
		out.println("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<a href=\"http://www.heise.de/newsticker/meldung/Studie-Mit-der-Bildung-steigt-die-Leidenschaft-fuer-Computerspiele-1708799.html/from/atom10\" title=\"Studie: Mit der Bildung steigt die Leidenschaft für Computerspiele\"><img  src=\"http://www.heise.de/imgs/18/9/1/8/9/6/7/d271db08dea74ff1.jpeg\" width=\"100\" height=\"75\" alt=\"\" title=\"\" style=\"float: left; margin-right: 15px; margin-top: 3px;\"/></a>");
		out.println("<p>Je höher der Bildungsabschluss, desto größer der Anteil von Spiele-Nutzern – diesen Zusammenhang will der IT-Branchenverband Bitkom durch eine von ihm beauftragte Umfrage belegen.</p>");
		out.println("</div>");
		out.println("</content>");
		out.println("</entry>");
	}

}
