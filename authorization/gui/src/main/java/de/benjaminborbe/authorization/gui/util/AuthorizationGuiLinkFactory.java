package de.benjaminborbe.authorization.gui.util;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.gui.AuthorizationGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class AuthorizationGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public AuthorizationGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget roleAddPermission(
		final HttpServletRequest request,
		final RoleIdentifier roleIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_PERMISSION_ADD, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_ROLE_ID, roleIdentifier.getId()), "add permission");
	}

	public Widget roleAddUser(final HttpServletRequest request, final RoleIdentifier roleIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ADD_ROLE, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_ROLE_ID, roleIdentifier.getId()), "add user");
	}

	public Widget userAddRole(final HttpServletRequest request, final UserIdentifier userIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ADD_ROLE, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_USER_ID, userIdentifier.getId()), "add role");
	}

	public Widget roleInfo(final HttpServletRequest request, final RoleIdentifier roleIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_INFO, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_ROLE_ID, String.valueOf(roleIdentifier)), roleIdentifier.getId());
	}

	public Widget roleCreate(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_CREATE, "add role");
	}

	public Widget roleDelete(final HttpServletRequest request, final RoleIdentifier roleIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_DELETE, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_ROLE_ID, String.valueOf(roleIdentifier)), "delete");
	}

	public Widget userInfo(final HttpServletRequest request, final UserIdentifier userIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_USER_INFO, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_USER_ID, userIdentifier.getId()), userIdentifier.getId());
	}

	public Widget roleRemoveUser(
		final HttpServletRequest request,
		final RoleIdentifier roleIdentifier,
		final UserIdentifier userIdentifier
	) throws MalformedURLException,
		UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_USER_REMOVE_ROLE, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_ROLE_ID, roleIdentifier.getId()).add(AuthorizationGuiConstants.PARAMETER_USER_ID, userIdentifier.getId()), "remove");
	}

	public Widget permissionList(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_PERMISSION_LIST, "permissions");
	}

	public Widget userList(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_USER_LIST, "users");
	}

	public Widget roleList(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_LIST, "roles");
	}

	public Widget roleRemovePermission(final HttpServletRequest request, final RoleIdentifier roleIdentifier, final PermissionIdentifier permissionIdentifier)
		throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_PERMISSION_REMOVE, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_ROLE_ID, roleIdentifier.getId()).add(AuthorizationGuiConstants.PARAMETER_PERMISSION_ID, permissionIdentifier), "remove");
	}

	public Widget permissionDelete(
		final HttpServletRequest request,
		final PermissionIdentifier permissionIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_PERMISSION_DELETE, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_PERMISSION_ID, permissionIdentifier), "delete").addConfirm("delete " + permissionIdentifier + "?");
	}

	public Widget permissionAddRole(
		final HttpServletRequest request,
		final PermissionIdentifier permissionIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_PERMISSION_ADD, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_PERMISSION_ID, permissionIdentifier.getId()), "add role");
	}

	public Widget permissionInfo(
		final HttpServletRequest request,
		final PermissionIdentifier permissionIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_PERMISSION_INFO, new MapParameter().add(
			AuthorizationGuiConstants.PARAMETER_PERMISSION_ID, permissionIdentifier.getId()), permissionIdentifier.getId());
	}
}
