package de.benjaminborbe.microblog.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.gui.MicroblogGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

@Singleton
public class MicroblogGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public MicroblogGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget sendConversation(final HttpServletRequest request, final MicroblogPostIdentifier microblogPostIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_CONVERSATION_SEND, new MapParameter().add(
				MicroblogGuiConstants.PARAMETER_POST_ID, String.valueOf(microblogPostIdentifier)), "send as conversation");
	}

	public Widget sendPost(final HttpServletRequest request, final MicroblogPostIdentifier microblogPostIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_POST_SEND, new MapParameter().add(
				MicroblogGuiConstants.PARAMETER_POST_ID, String.valueOf(microblogPostIdentifier)), "send as post");
	}

	public Widget refreshPost(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_POST_REFRESH, "refresh");
	}

	public Widget updatePost(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_POST_UPDATE, "update");
	}

	public Widget deleteNotification(final HttpServletRequest request, final UserIdentifier userIdentifier, final String keyword) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_NOTIFICATION_DEACTIVATE, new MapParameter().add(
				MicroblogGuiConstants.PARAEMTER_NOTIFICATION_KEYWORD, keyword).add(MicroblogGuiConstants.PARAEMTER_NOTIFICATION_LOGIN, String.valueOf(userIdentifier)), "delete");
	}

	public Widget notificationList(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_NOTIFICATION_LIST, "notifications");
	}
}
