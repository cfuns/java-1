package de.benjaminborbe.kiosk.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

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
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
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

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final long ean;
			try {
				ean = parseUtil.parseLong(request.getParameter(KioskGuiConstants.PARAMETER_EAN_NUMBER));
			}
			catch (final ParseException e) {
				printError(response, "parameter " + KioskGuiConstants.PARAMETER_EAN_NUMBER + " missing or invalid number");
				return;
			}
			final long customer;
			try {
				customer = parseUtil.parseLong(request.getParameter(KioskGuiConstants.PARAMETER_CUSTOMER_NUMBER));
			}
			catch (final ParseException e) {
				printError(response, "parameter " + KioskGuiConstants.PARAMETER_CUSTOMER_NUMBER + " missing or invalid number");
				return;
			}

			logger.debug("book - customer: " + customer + " ean: " + ean);
			kioskService.book(customer, ean);
			final JSONObject jsonObject = new JSONObjectSimple();
			jsonObject.put("result", "success");
			printJson(response, jsonObject);
		}
		catch (final KioskServiceException e) {
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
