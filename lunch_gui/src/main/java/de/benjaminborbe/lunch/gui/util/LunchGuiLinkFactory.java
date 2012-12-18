package de.benjaminborbe.lunch.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.lunch.gui.LunchGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

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
}
