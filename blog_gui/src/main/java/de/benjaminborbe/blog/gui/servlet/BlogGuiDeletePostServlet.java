package de.benjaminborbe.blog.gui.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.blog.api.BlogPostDeleteException;
import de.benjaminborbe.blog.api.BlogService;
import de.benjaminborbe.blog.api.BlogServiceException;
import de.benjaminborbe.blog.gui.BlogGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class BlogGuiDeletePostServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Blog";

	private final BlogService blogService;

	private final AuthenticationService authenticationService;

	@Inject
	public BlogGuiDeletePostServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final BlogService blogService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, redirectUtil, urlUtil);
		this.authenticationService = authenticationService;
		this.blogService = blogService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final String title = request.getParameter(BlogGuiConstants.PARAMETER_BLOG_POST_TITLE);
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (title != null) {
				try {
					blogService.deleteBlogPost(sessionIdentifier, blogService.createBlogPostIdentifier(sessionIdentifier, title));
					throw new RedirectException(request.getContextPath() + "/" + BlogGuiConstants.NAME);
				}
				catch (final BlogPostDeleteException e) {
					widgets.add("delete blogPost failed");
				}
			}
			return widgets;
		}
		catch (final BlogServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
