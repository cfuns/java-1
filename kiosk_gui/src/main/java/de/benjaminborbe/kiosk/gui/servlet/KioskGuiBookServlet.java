package de.benjaminborbe.kiosk.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.api.KioskServiceException;
import de.benjaminborbe.kiosk.gui.KioskGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteJsonServlet;

@Singleton
public class KioskGuiBookServlet extends WebsiteJsonServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final ParseUtil parseUtil;

	private final KioskService kioskService;

	private final Logger logger;

	@Inject
	public KioskGuiBookServlet(
			final Logger logger,
			final KioskService kioskService,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.kioskService = kioskService;
		this.parseUtil = parseUtil;
		this.logger = logger;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final String customerNumber = request.getParameter(KioskGuiConstants.PARAMETER_CUSTOMER_NUMBER);
			final String eanNumber = request.getParameter(KioskGuiConstants.PARAMETER_EAN_NUMBER);
			logger.debug("book - customer: " + customerNumber + " ean: " + eanNumber);
			final long ean = parseUtil.parseLong(eanNumber);
			final long customer = parseUtil.parseLong(customerNumber);
			kioskService.book(customer, ean);
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("result", "success");
			printJson(response, jsonObject);
		}
		catch (final KioskServiceException e) {
			logger.warn(e.getClass().getName(), e);
			printException(response, e);
		}
		catch (final ParseException e) {
			logger.warn(e.getClass().getName(), e);
			printException(response, e);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
