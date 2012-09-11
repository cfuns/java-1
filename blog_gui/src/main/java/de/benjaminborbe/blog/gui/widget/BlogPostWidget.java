package de.benjaminborbe.blog.gui.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.blog.api.BlogPost;
import de.benjaminborbe.blog.gui.BlogGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

public class BlogPostWidget implements Widget {

	private final BlogPost blogPost;

	private final UrlUtil urlUtil;

	public BlogPostWidget(final BlogPost blogPost, final UrlUtil urlUtil) {
		this.blogPost = blogPost;
		this.urlUtil = urlUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws PermissionDeniedException, IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H2Widget(blogPost.getTitle()));
		widgets.add(blogPost.getContent());

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
