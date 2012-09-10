package de.benjaminborbe.blog.gui.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.blog.api.BlogPost;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;

public class BlogPostWidget implements Widget {

	private final BlogPost blogPost;

	public BlogPostWidget(final BlogPost blogPost) {
		this.blogPost = blogPost;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws PermissionDeniedException, IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H2Widget(blogPost.getTitle()));
		widgets.add(blogPost.getContent());
		widgets.render(request, response, context);
	}
}
