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

	public String getPasswordResetUrl(final HttpServletRequest request) throws UnsupportedEncodingException {
		final StringBuilder sb = new StringBuilder();
		sb.append(urlUtil.buildBaseUrl(request));
		sb.append("/");
		sb.append(AuthenticationGuiConstants.NAME);
		sb.append(AuthenticationGuiConstants.URL_USER_PASSWORD_LOST_RESET);
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

	public Widget userCreate(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_CREATE, "create user");
	}

	public Widget userDelete(final HttpServletRequest request, final UserIdentifier userIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_DELETE, new MapParameter().add(
				AuthenticationGuiConstants.PARAMETER_USER_ID, String.valueOf(userIdentifier)), "delete").addConfirm("delete " + userIdentifier + "?");
	}

	public String userListUrl(final HttpServletRequest request) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_LIST, new MapParameter());
	}

	public Widget userPasswordChange(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_PASSWORD_CHANGE, "change password");
	}

	public Widget userPasswordLost(final HttpServletRequest request, final UserIdentifier userIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_PASSWORD_LOST, new MapParameter().add(
				AuthenticationGuiConstants.PARAMETER_USER_ID, String.valueOf(userIdentifier)), "reset password");
	}

	public Widget userProfile(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_PROFILE, "user profile");
	}

	public String userProfileUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_PROFILE;
	}

	public Widget userSwitch(final HttpServletRequest request, final UserIdentifier userIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthenticationGuiConstants.NAME + AuthenticationGuiConstants.URL_USER_SWITCH, new MapParameter().add(
				AuthenticationGuiConstants.PARAMETER_USER_ID, String.valueOf(userIdentifier)), "switch user");
	}

	public Widget userView(final HttpServletRequest request, final UserIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/authorization/user/info", new MapParameter().add(AuthenticationGuiConstants.PARAMETER_USER_ID, id), id.getId());
	}
}
