package de.benjaminborbe.lunch.gui.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.api.LunchServiceException;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.LiWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class LunchGuiServlet extends WebsiteHtmlServlet {

	private final class SortLunchs implements Comparator<Lunch> {

		@Override
		public int compare(final Lunch arg0, final Lunch arg1) {
			return arg1.getDate().compareTo(arg0.getDate());
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Lunch";

	private final LunchService lunchService;

	private final DateUtil dateUtil;

	@Inject
	public LunchGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final LunchService lunchService,
			final DateUtil dateUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, redirectUtil, urlUtil);
		this.dateUtil = dateUtil;
		this.lunchService = lunchService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<Lunch> lunchs = new ArrayList<Lunch>(lunchService.getLunchs(sessionIdentifier));
			Collections.sort(lunchs, new SortLunchs());
			final UlWidget ul = new UlWidget();
			for (final Lunch lunch : lunchs) {
				final StringWriter content = new StringWriter();
				content.append(dateUtil.dateString(lunch.getDate()));
				content.append(" ");
				content.append(lunch.getName());
				content.append(" (");
				content.append((lunch.isSubscribed() ? "subscribed" : "not subscribed"));
				content.append(")");

				final LiWidget li;
				if (dateUtil.isToday(lunch.getDate())) {
					li = new LiWidget(new TagWidget("b", content.toString()));
				}
				else {
					li = new LiWidget(content.toString());
				}

				li.addAttribute("class", (lunch.isSubscribed() ? "subscribed" : "notsubscribed"));

				ul.add(li);
			}
			widgets.add(ul);
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final LunchServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		final Collection<CssResource> result = super.getCssResources(request, response);
		result.add(new CssResourceImpl(request.getContextPath() + "/lunch/css/style.css"));
		return result;
	}
}
