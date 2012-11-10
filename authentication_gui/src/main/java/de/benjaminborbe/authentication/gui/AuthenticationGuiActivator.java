package de.benjaminborbe.authentication.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.gui.guice.AuthenticationGuiModules;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiChangePasswordServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiLoginServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiLogoutServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiRegisterServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiStatusServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUnregisterServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserListServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class AuthenticationGuiActivator extends HttpBundleActivator {

	@Inject
	private AuthenticationGuiStatusServlet authenticationGuiStatusServlet;

	@Inject
	private AuthenticationGuiLoginServlet authenticationGuiLoginServlet;

	@Inject
	private AuthenticationGuiServlet authenticationGuiServlet;

	@Inject
	private AuthenticationGuiLogoutServlet authenticationGuiLogoutServlet;

	@Inject
	private AuthenticationGuiUnregisterServlet authenticationGuiUnregisterServlet;

	@Inject
	private AuthenticationGuiRegisterServlet authenticationGuiRegisterServlet;

	@Inject
	private AuthenticationGuiChangePasswordServlet authenticationGuiChangePasswordServlet;

	@Inject
	private AuthenticationGuiUserListServlet authenticationGuiUserListServlet;

	public AuthenticationGuiActivator() {
		super(AuthenticationGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new AuthenticationGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(authenticationGuiServlet, AuthenticationGuiConstants.URL_SLASH));
		result.add(new ServletInfo(authenticationGuiLoginServlet, AuthenticationGuiConstants.URL_LOGIN));
		result.add(new ServletInfo(authenticationGuiStatusServlet, AuthenticationGuiConstants.URL_STATUS));
		result.add(new ServletInfo(authenticationGuiLogoutServlet, AuthenticationGuiConstants.URL_LOGOUT));
		result.add(new ServletInfo(authenticationGuiRegisterServlet, AuthenticationGuiConstants.URL_REGISTER));
		result.add(new ServletInfo(authenticationGuiUnregisterServlet, AuthenticationGuiConstants.URL_UNREGISTER));
		result.add(new ServletInfo(authenticationGuiChangePasswordServlet, AuthenticationGuiConstants.URL_CHANGE_PASSWORD));
		result.add(new ServletInfo(authenticationGuiUserListServlet, AuthenticationGuiConstants.URL_USER_LIST));
		return result;
	}

}
