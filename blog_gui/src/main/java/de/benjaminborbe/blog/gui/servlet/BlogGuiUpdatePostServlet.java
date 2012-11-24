package de.benjaminborbe.blog.gui.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.blog.api.BlogPost;
import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.blog.api.BlogPostNotFoundException;
import de.benjaminborbe.blog.api.BlogService;
import de.benjaminborbe.blog.api.BlogServiceException;
import de.benjaminborbe.blog.gui.BlogGuiConstants;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormInputTextareaWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class BlogGuiUpdatePostServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Blog - Update Post";

	private final BlogService blogService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public BlogGuiUpdatePostServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final BlogService blogService,
			final AuthorizationService authorizationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.blogService = blogService;
		this.logger = logger;
		this.authenticationService = authenticationService;
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

			final String id = request.getParameter(BlogGuiConstants.PARAMETER_BLOG_POST_ID);
			final String title = request.getParameter(BlogGuiConstants.PARAMETER_BLOG_POST_TITLE);
			final String content = request.getParameter(BlogGuiConstants.PARAMETER_BLOG_POST_CONTENT);
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final BlogPostIdentifier blogPostIdentifier = blogService.createBlogPostIdentifier(sessionIdentifier, id);
			final BlogPost blogPost = blogService.getBlogPost(sessionIdentifier, blogPostIdentifier);
			if (title != null && content != null) {
				try {
					blogService.updateBlogPost(sessionIdentifier, blogPostIdentifier, title, content);
					logger.debug("new BlogPost updated");
					throw new RedirectException(request.getContextPath() + "/" + BlogGuiConstants.NAME);
				}
				catch (final ValidationException e) {
					widgets.add("update blogPost failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(BlogGuiConstants.PARAMETER_BLOG_POST_ID));
			formWidget.addFormInputWidget(new FormInputTextWidget(BlogGuiConstants.PARAMETER_BLOG_POST_TITLE).addDefaultValue(blogPost.getTitle()).addLabel("Title")
					.addPlaceholder("title..."));
			formWidget.addFormInputWidget(new FormInputTextareaWidget(BlogGuiConstants.PARAMETER_BLOG_POST_CONTENT).addDefaultValue(blogPost.getContent()).addLabel("Content")
					.addPlaceholder("content..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("update"));
			widgets.add(formWidget);
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final BlogServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final BlogPostNotFoundException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		final Collection<CssResource> result = super.getCssResources(request, response);
		result.add(new CssResourceImpl(request.getContextPath() + "/blog/css/style.css"));
		return result;
	}

}
