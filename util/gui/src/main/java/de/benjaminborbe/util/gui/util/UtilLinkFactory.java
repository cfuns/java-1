package de.benjaminborbe.util.gui.util;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.util.gui.UtilGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

public class UtilLinkFactory {

	@Inject
	public UtilLinkFactory() {
	}

	public Widget time(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_TIME, "Current Time");
	}

	public Widget angularJsSample(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_HTML_ANGULARJS, "AngularJS Sample");
	}

	public Widget qunitSample(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_QUNIT, "QUnit Sample");
	}

	public Widget calc(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_CALC, "Calc");
	}

	public Widget dayDiff(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_DAY_DIFF, "DayDiff");
	}

	public Widget log(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_LOG, "Log");
	}

	public Widget passwordGenerator(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_PASSWORD_GENERATOR, "Password Generator");
	}

	public Widget penMe(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_PENME, "penMe");
	}

	public Widget penTest(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_PENTEST, "penTest");
	}

	public Widget timeConvert(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_TIME_CONVERT, "TimeConvert");
	}

	public Widget uuidGenerator(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_UUID_GENERATOR, "UUID Generator");
	}

	public Widget dumpRequest(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + UtilGuiConstants.NAME + UtilGuiConstants.URL_DUMP_REQUEST, "Dump Request");
	}
}
