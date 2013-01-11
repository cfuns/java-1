package de.benjaminborbe.authorization.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.authorization.gui.guice.AuthorizationGuiModules;
import de.benjaminborbe.authorization.gui.service.AuthorizationGuiNavigationEntry;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiPermissionDeniedServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiPermissionListServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleAddPermissionServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleCreateServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleInfoServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleListServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleRemovePermissionServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleDeleteServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiUserAddRoleServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiUserInfoServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiUserListServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiUserRemoveRoleServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class AuthorizationGuiActivator extends HttpBundleActivator {

	@Inject
	private AuthorizationGuiPermissionDeniedServlet authorizationGuiPermissionDeniedServlet;

	@Inject
	private AuthorizationGuiPermissionListServlet authorizationGuiPermissionListServlet;

	@Inject
	private AuthorizationGuiServlet authorizationGuiServlet;

	@Inject
	private AuthorizationGuiRoleListServlet authorizationGuiRoleListServlet;

	@Inject
	private AuthorizationGuiRoleCreateServlet authorizationGuiRoleCreateServlet;

	@Inject
	private AuthorizationGuiRoleDeleteServlet authorizationGuiRoleRemoveServlet;

	@Inject
	private AuthorizationGuiUserAddRoleServlet authorizationGuiUserAddRoleServlet;

	@Inject
	private AuthorizationGuiUserRemoveRoleServlet authorizationGuiUserRemoveRoleServlet;

	@Inject
	private AuthorizationGuiRoleAddPermissionServlet authorizationGuiRoleAddPermissionServlet;

	@Inject
	private AuthorizationGuiRoleRemovePermissionServlet authorizationGuiRoleRemovePermissionServlet;

	@Inject
	private AuthorizationGuiRoleInfoServlet authorizationGuiRoleInfoServlet;

	@Inject
	private AuthorizationGuiUserInfoServlet authorizationGuiUserInfoServlet;

	@Inject
	private AuthorizationGuiUserListServlet authorizationGuiUserListServlet;

	@Inject
	private AuthorizationGuiNavigationEntry authorizationGuiNavigationEntry;

	public AuthorizationGuiActivator() {
		super(AuthorizationGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new AuthorizationGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(authorizationGuiServlet, AuthorizationGuiConstants.URL_SLASH));
		result.add(new ServletInfo(authorizationGuiRoleListServlet, AuthorizationGuiConstants.URL_ROLE_LIST));
		result.add(new ServletInfo(authorizationGuiRoleCreateServlet, AuthorizationGuiConstants.URL_ROLE_CREATE));
		result.add(new ServletInfo(authorizationGuiRoleRemoveServlet, AuthorizationGuiConstants.URL_ROLE_DELETE));
		result.add(new ServletInfo(authorizationGuiUserAddRoleServlet, AuthorizationGuiConstants.URL_ADD_ROLE));
		result.add(new ServletInfo(authorizationGuiUserRemoveRoleServlet, AuthorizationGuiConstants.URL_USER_REMOVE_ROLE));
		result.add(new ServletInfo(authorizationGuiRoleAddPermissionServlet, AuthorizationGuiConstants.URL_ROLE_ADD_PERMISSION));
		result.add(new ServletInfo(authorizationGuiRoleRemovePermissionServlet, AuthorizationGuiConstants.URL_ROLE_PERMISSION_REMOVE));
		result.add(new ServletInfo(authorizationGuiUserListServlet, AuthorizationGuiConstants.URL_USER_LIST));
		result.add(new ServletInfo(authorizationGuiUserInfoServlet, AuthorizationGuiConstants.URL_USER_INFO));
		result.add(new ServletInfo(authorizationGuiRoleInfoServlet, AuthorizationGuiConstants.URL_ROLE_INFO));
		result.add(new ServletInfo(authorizationGuiPermissionListServlet, AuthorizationGuiConstants.URL_PERMISSION_LIST));
		result.add(new ServletInfo(authorizationGuiPermissionDeniedServlet, AuthorizationGuiConstants.URL_PERMISSION_DENIED));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, authorizationGuiNavigationEntry));
		return result;
	}

}
