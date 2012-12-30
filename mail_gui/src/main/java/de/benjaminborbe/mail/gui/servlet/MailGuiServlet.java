package de.benjaminborbe.mail.gui.servlet;

import java.io.IOException;
import java.net.SocketException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.gui.MailGuiConstants;
import de.benjaminborbe.mail.gui.util.MailLinkFactory;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.NetUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class MailGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Mail";

	private final MailService mailService;

	private final Logger logger;

	private final MailLinkFactory mailLinkFactory;

	private final NetUtil netUtil;

	private final CalendarUtil calendarUtil;

	@Inject
	public MailGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final MailService mailService,
			final AuthorizationService authorizationService,
			final MailLinkFactory mailLinkFactory,
			final NetUtil netUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.calendarUtil = calendarUtil;
		this.mailService = mailService;
		this.logger = logger;
		this.mailLinkFactory = mailLinkFactory;
		this.netUtil = netUtil;
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

			final String sendTestMail = request.getParameter(MailGuiConstants.PARAMETER_SEND_TESTMAIL);

			if (sendTestMail != null) {
				final MailDto mail = buildMail();
				mailService.send(mail);
				widgets.add("testmail sent");
				widgets.add(new BrWidget());
			}
			widgets.add(mailLinkFactory.sendTestMail(request));

			return widgets;
		}
		catch (final MailServiceException e) {
			return new ExceptionWidget(e);
		}
	}

	protected MailDto buildMail() {
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "TestMail";
		final StringBuilder sb = new StringBuilder();
		sb.append("TestMailContent\n");
		sb.append("\n");
		try {
			final Collection<String> hostnames = netUtil.getHostnames();
			sb.append("Hostnames:\n");
			for (final String hostname : hostnames) {
				sb.append(hostname);
				sb.append("\n");
			}
			sb.append("\n");
		}
		catch (final SocketException e) {
		}
		sb.append("CurrentTime:\n");
		sb.append(calendarUtil.toDateTimeString(calendarUtil.now()));
		sb.append("\n");

		return new MailDto(from, to, subject, sb.toString(), "text/plain");
	}
}
