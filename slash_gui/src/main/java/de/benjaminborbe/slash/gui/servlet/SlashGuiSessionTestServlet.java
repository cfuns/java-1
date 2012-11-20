package de.benjaminborbe.slash.gui.servlet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class SlashGuiSessionTestServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String SESSION_KEY = "testCounter";

	private static final String TITLE = "Session - Test";

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public SlashGuiSessionTestServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	protected boolean isLoginRequired() {
		return false;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		widgets.add("Session-ID: " + request.getSession().getId());
		widgets.add(new BrWidget());
		final HttpSession session = request.getSession();
		final long counter = parseUtil.parseLong(String.valueOf(session.getAttribute(SESSION_KEY)), 0);
		widgets.add("Counter: " + counter);
		session.setAttribute(SESSION_KEY, String.valueOf(counter + 1));
		widgets.add(new BrWidget());
		widgets.add("Hostnames: ");
		try {
			final UlWidget ul = new UlWidget();
			final List<String> hostnames = new ArrayList<String>(getHostnames());
			Collections.sort(hostnames);
			for (final String hostname : hostnames) {
				ul.add(hostname);
			}
			widgets.add(ul);
		}
		catch (final SocketException e) {
			widgets.add("getHostnames failed!");
			widgets.add(new ExceptionWidget(e));
		}
		return widgets;
	}

	private Collection<String> getHostnames() throws SocketException {
		final Set<String> result = new HashSet<String>();
		final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			final NetworkInterface nic = interfaces.nextElement();
			final Enumeration<InetAddress> addresses = nic.getInetAddresses();
			while (addresses.hasMoreElements()) {
				final InetAddress address = addresses.nextElement();
				if (!address.isLoopbackAddress()) {
					final String hostname = address.getHostName();
					if (hostname != null) {
						result.add(hostname);
					}
				}
			}
		}
		return result;
	}
}
