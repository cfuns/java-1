package de.benjaminborbe.blog.gui.widget;

import de.benjaminborbe.blog.gui.BlogGuiConstants;
import de.benjaminborbe.blog.gui.atom.Entry;
import de.benjaminborbe.blog.gui.atom.Feed;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BlogGuiAtomWidget implements Widget {

	private final Feed feed;

	public BlogGuiAtomWidget(final Feed feed) {
		this.feed = feed;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		response.setContentType(BlogGuiConstants.ATOM_CONTENT_TYPE);
		final PrintWriter out = response.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		out.println("<feed xmlns=\"http://www.w3.org/2005/Atom\">");
		out.println("<id>" + feed.getId() + "</id>");
		out.println("<title>" + feed.getTitle() + "</title>");
		out.println("<subtitle>" + feed.getSubtitle() + "</subtitle>");
		out.println("<link href=\"" + feed.getLink() + "\" />");
		out.println("<link rel=\"self\" href=\"" + feed.getLink() + "\" />");
		out.println("<updated>" + feed.getUpdated() + "</updated>");
		out.println("<author>");
		out.println("<name>" + feed.getAuthor().getName() + "</name>");
		out.println("</author>");
		for (final Entry entry : feed.getEntries()) {
			out.println("<entry>");
			out.println("<id>" + entry.getId() + "</id>");
			out.println("<title>" + entry.getTitle() + "</title>");
			out.println("<link href=\"" + entry.getLink() + "\" />");
			out.println("<published>" + entry.getPublished() + "</published>");
			out.println("<updated>" + entry.getUpdated() + "</updated>");
			out.println("<summary>" + entry.getSummary() + "</summary>");
			out.println("<content type=\"xhtml\">");
			out.println("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
			out.println(entry.getContent());
			out.println("</div>");
			out.println("</content>");
			out.println("</entry>");
		}
		out.println("</feed>");
	}

}
