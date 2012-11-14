package de.benjaminborbe.authentication.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.gui.AuthenticationGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class AuthenticationGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public AuthenticationGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget changeUser(final HttpServletRequest request, final UserIdentifier userIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_SWITCH_USER, new MapParameter().add(
				AuthenticationGuiConstants.PARAMETER_USER_ID, String.valueOf(userIdentifier)), "switch user");
	}

}
