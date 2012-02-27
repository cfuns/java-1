package de.benjaminborbe.authorization.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.authorization.gui.guice.AuthorizationGuiModules;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleAddPermissionServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleCreateServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleInfoServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleListServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleRemovePermissionServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiRoleRemoveServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiUserAddRoleServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiUserInfoServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiUserListServlet;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiUserRemoveRoleServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class AuthorizationGuiActivator extends HttpBundleActivator {

	@Inject
	private AuthorizationGuiServlet authorizationGuiServlet;

	@Inject
	private AuthorizationGuiRoleListServlet authorizationGuiRoleListServlet;

	@Inject
	private AuthorizationGuiRoleCreateServlet authorizationGuiRoleCreateServlet;

	@Inject
	private AuthorizationGuiRoleRemoveServlet authorizationGuiRoleRemoveServlet;

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

	public AuthorizationGuiActivator() {
		super("authorization");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new AuthorizationGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(authorizationGuiServlet, "/"));
		result.add(new ServletInfo(authorizationGuiRoleListServlet, "/role"));
		result.add(new ServletInfo(authorizationGuiRoleCreateServlet, "/role/create"));
		result.add(new ServletInfo(authorizationGuiRoleRemoveServlet, "/role/remove"));
		result.add(new ServletInfo(authorizationGuiUserAddRoleServlet, "/user/addRole"));
		result.add(new ServletInfo(authorizationGuiUserRemoveRoleServlet, "/user/removeRole"));
		result.add(new ServletInfo(authorizationGuiRoleAddPermissionServlet, "/role/addPermission"));
		result.add(new ServletInfo(authorizationGuiRoleRemovePermissionServlet, "/role/removePermission"));
		result.add(new ServletInfo(authorizationGuiUserListServlet, "/user"));
		result.add(new ServletInfo(authorizationGuiUserInfoServlet, "/user/info"));
		result.add(new ServletInfo(authorizationGuiRoleInfoServlet, "/role/info"));
		return result;
	}

}
