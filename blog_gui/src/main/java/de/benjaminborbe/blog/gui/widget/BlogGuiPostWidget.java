package de.benjaminborbe.blog.gui.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.blog.api.BlogPost;
import de.benjaminborbe.blog.gui.BlogGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.br.BrWidget;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

public class BlogGuiPostWidget implements Widget {

	private final BlogPost blogPost;

	private final UrlUtil urlUtil;

	private final CalendarUtil calendarUtil;

	public BlogGuiPostWidget(final BlogPost blogPost, final UrlUtil urlUtil, final CalendarUtil calendarUtil) {
		this.blogPost = blogPost;
		this.urlUtil = urlUtil;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H2Widget(blogPost.getTitle()));
		final UlWidget metaInfos = new UlWidget();
		metaInfos.add("author: " + blogPost.getCreator());
		metaInfos.add("created: " + (blogPost.getCreated() != null ? calendarUtil.toDateTimeString(blogPost.getCreated()) : "-"));
		metaInfos.add("modified: " + (blogPost.getModified() != null ? calendarUtil.toDateTimeString(blogPost.getModified()) : "-"));
		widgets.add(metaInfos);
		final String content = blogPost.getContent();
		final String[] parts = content.split("\n");
		for (final String part : parts) {
			widgets.add(part);
			widgets.add(new BrWidget());
		}

		final UlWidget options = new UlWidget();
		options.addAttribute("class", "options");
		options.add(new LinkRelativWidget(urlUtil, request, "/" + BlogGuiConstants.NAME + BlogGuiConstants.POST_UPDATE_URL, new MapChain<String, String>().add(
				BlogGuiConstants.PARAMETER_BLOG_POST_ID, blogPost.getId().getId()), "edit"));
		options.add(new LinkRelativWidget(urlUtil, request, "/" + BlogGuiConstants.NAME + BlogGuiConstants.POST_DELETE_URL, new MapChain<String, String>().add(
				BlogGuiConstants.PARAMETER_BLOG_POST_ID, blogPost.getId().getId()), "delete"));
		widgets.add(options);

		final DivWidget div = new DivWidget(widgets);
		div.addAttribute("class", "blogpost");
		div.render(request, response, context);
	}
}
