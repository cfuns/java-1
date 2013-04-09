package de.benjaminborbe.notification.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.gui.NotificationGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class NotificationGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public NotificationGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget send(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + NotificationGuiConstants.NAME + NotificationGuiConstants.URL_SEND, "send notification");
	}

	public Widget remove(final HttpServletRequest request, final NotificationMediaIdentifier notificationMediaIdentifier, final NotificationTypeIdentifier notificationTypeIdentifier)
			throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + NotificationGuiConstants.NAME + NotificationGuiConstants.URL_REMOVE, new MapParameter().add(
				NotificationGuiConstants.PARAMETER_TYPE, notificationTypeIdentifier).add(NotificationGuiConstants.PARAMETER_MEDIA, notificationMediaIdentifier),
				notificationMediaIdentifier + " is active");
	}

	public Widget add(final HttpServletRequest request, final NotificationMediaIdentifier notificationMediaIdentifier, final NotificationTypeIdentifier notificationTypeIdentifier)
			throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + NotificationGuiConstants.NAME + NotificationGuiConstants.URL_ADD, new MapParameter().add(
				NotificationGuiConstants.PARAMETER_TYPE, notificationTypeIdentifier).add(NotificationGuiConstants.PARAMETER_MEDIA, notificationMediaIdentifier),
				notificationMediaIdentifier + " is not active");
	}

}
