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

	public String userProfileUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_PROFILE;
	}

	public Widget changePassword(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_CHANGE_PASSWORD, "change password");
	}

	public Widget userProfile(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_PROFILE, "user profile");
	}

	public String getEmailValidationUrl(final HttpServletRequest request) throws UnsupportedEncodingException {
		final StringBuilder sb = new StringBuilder();
		sb.append(urlUtil.buildBaseUrl(request));
		sb.append("/");
		sb.append(AuthenticationGuiConstants.NAME);
		sb.append(AuthenticationGuiConstants.URL_EMAIL_VALIDATION);
		sb.append("?");
		sb.append(AuthenticationGuiConstants.PARAMETER_EMAIL_VERIFY_TOKEN);
		sb.append("=%s&");
		sb.append(AuthenticationGuiConstants.PARAMETER_USER_ID);
		sb.append("=%s");
		return sb.toString();
	}

	public String getShortenUrl(final HttpServletRequest request) {
		return urlUtil.buildBaseUrl(request) + "/s/%s";
	}

	public Widget userView(final HttpServletRequest request, final UserIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/authorization/user/info", new MapParameter().add("user_id", id), id.getId());
	}
}
