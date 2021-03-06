package de.benjaminborbe.lunch.gui.util;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Calendar;

public class LunchGuiLinkFactory {

	private final UrlUtil urlUtil;

	private final CalendarUtil calendarUtil;

	@Inject
	public LunchGuiLinkFactory(final UrlUtil urlUtil, final CalendarUtil calendarUtil) {
		this.urlUtil = urlUtil;
		this.calendarUtil = calendarUtil;
	}

	public Widget bookingSubDay(final HttpServletRequest request, final Calendar calendar) throws MalformedURLException, UnsupportedEncodingException {
		final String dateString = calendarUtil.toDateString(calendarUtil.addDays(calendar, -1));
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_BOOKING, new MapParameter().add(LunchGuiConstants.PARAMETER_BOOKING_DATE,
			dateString), "previous day");
	}

	public Widget bookingAddDay(final HttpServletRequest request, final Calendar calendar) throws MalformedURLException, UnsupportedEncodingException {
		final String dateString = calendarUtil.toDateString(calendarUtil.addDays(calendar, 1));
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_BOOKING, new MapParameter().add(LunchGuiConstants.PARAMETER_BOOKING_DATE,
			dateString), "next day");
	}

	public Widget bookingToday(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_BOOKING, new MapParameter(), "today");
	}

	public Widget bookedForDay(final HttpServletRequest request, final Calendar calendar) throws MalformedURLException, UnsupportedEncodingException {
		final String dateString = calendarUtil.toDateString(calendar);
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_BOOKED, new MapParameter().add(LunchGuiConstants.PARAMETER_BOOKED_DATE,
			dateString), "booked");
	}

	public Widget bookedToday(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_BOOKED, new MapParameter(), "today");
	}

	public Widget bookedSubDay(final HttpServletRequest request, final Calendar calendar) throws MalformedURLException, UnsupportedEncodingException {
		final String dateString = calendarUtil.toDateString(calendarUtil.addDays(calendar, -1));
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_BOOKED, new MapParameter().add(LunchGuiConstants.PARAMETER_BOOKED_DATE,
			dateString), "previous day");
	}

	public Widget bookedAddDay(final HttpServletRequest request, final Calendar calendar) throws MalformedURLException, UnsupportedEncodingException {
		final String dateString = calendarUtil.toDateString(calendarUtil.addDays(calendar, 1));
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_BOOKED, new MapParameter().add(LunchGuiConstants.PARAMETER_BOOKED_DATE,
			dateString), "next day");
	}

	public Widget deactivateNotification(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_NOTIFICATION, new MapParameter().add(LunchGuiConstants.PARAMETER_ACTION,
			LunchGuiConstants.ACTION_DEACTIVATE), "deactivate notification");
	}

	public Widget activateNotification(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + LunchGuiConstants.NAME + LunchGuiConstants.URL_NOTIFICATION, new MapParameter().add(LunchGuiConstants.PARAMETER_ACTION,
			LunchGuiConstants.ACTION_ACTIVATE), "activate notification");
	}
}
