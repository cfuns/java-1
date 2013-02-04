package de.benjaminborbe.cron.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronIdentifier;
import de.benjaminborbe.cron.gui.CronGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class CronGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public CronGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget history(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + CronGuiConstants.NAME + CronGuiConstants.URL_LATEST, new MapParameter(), "history");
	}

	public Widget manage(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + CronGuiConstants.NAME + CronGuiConstants.URL_MANAGE, new MapParameter(), "manage");
	}

	public Widget list(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + CronGuiConstants.NAME + CronGuiConstants.URL_LIST, new MapParameter(), "list");
	}

	public Widget triggerCron(final HttpServletRequest request, final CronIdentifier cron) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + CronGuiConstants.NAME + CronGuiConstants.URL_CRON_TRIGGER,
				new MapParameter().add(CronGuiConstants.PARAMETER_CRON_ID, cron), "trigger");
	}
}
