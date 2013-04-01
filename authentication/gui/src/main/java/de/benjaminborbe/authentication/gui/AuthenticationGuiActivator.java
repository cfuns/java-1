package de.benjaminborbe.authentication.gui;

import com.google.inject.Inject;
import de.benjaminborbe.authentication.gui.config.AuthenticationGuiConfig;
import de.benjaminborbe.authentication.gui.guice.AuthenticationGuiModules;
import de.benjaminborbe.authentication.gui.service.AuthenticationGuiNavigationEntry;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiLoginServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiLogoutServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiRegisterServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiStatusServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiSwitchUserServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUnregisterServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserCreateServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserDeleteServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserListServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserPasswordChangeServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserPasswordLostEmailServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserPasswordLostResetServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserProfileServlet;
import de.benjaminborbe.authentication.gui.servlet.AuthenticationGuiUserVerifyEmailServlet;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthenticationGuiActivator extends HttpBundleActivator {

	@Inject
	private AuthenticationGuiUserPasswordLostResetServlet authenticationGuiUserPasswordLostResetServlet;

	@Inject
	private AuthenticationGuiUserPasswordLostEmailServlet authenticationGuiUserPasswordLostServlet;

	@Inject
	private AuthenticationGuiUserDeleteServlet authenticationGuiUserDeleteServlet;

	@Inject
	private AuthenticationGuiUserCreateServlet authenticationGuiUserCreateServlet;

	@Inject
	private AuthenticationGuiNavigationEntry authenticationGuiNavigationEntry;

	@Inject
	private AuthenticationGuiUserVerifyEmailServlet authenticationGuiVerifyEmailServlet;

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
	private AuthenticationGuiUserPasswordChangeServlet authenticationGuiChangePasswordServlet;

	@Inject
	private AuthenticationGuiUserListServlet authenticationGuiUserListServlet;

	@Inject
	private AuthenticationGuiSwitchUserServlet authenticationGuiSwitchUserServlet;

	@Inject
	private AuthenticationGuiUserProfileServlet authenticationGuiProfileServlet;

	@Inject
	private AuthenticationGuiConfig authenticationGuiConfig;

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
		result.add(new ServletInfo(authenticationGuiStatusServlet, AuthenticationGuiConstants.URL_USER_STATUS));
		result.add(new ServletInfo(authenticationGuiLogoutServlet, AuthenticationGuiConstants.URL_LOGOUT));
		result.add(new ServletInfo(authenticationGuiRegisterServlet, AuthenticationGuiConstants.URL_REGISTER));
		result.add(new ServletInfo(authenticationGuiUnregisterServlet, AuthenticationGuiConstants.URL_UNREGISTER));
		result.add(new ServletInfo(authenticationGuiUserListServlet, AuthenticationGuiConstants.URL_USER_LIST));
		result.add(new ServletInfo(authenticationGuiSwitchUserServlet, AuthenticationGuiConstants.URL_USER_SWITCH));
		result.add(new ServletInfo(authenticationGuiProfileServlet, AuthenticationGuiConstants.URL_USER_PROFILE));
		result.add(new ServletInfo(authenticationGuiVerifyEmailServlet, AuthenticationGuiConstants.URL_EMAIL_VALIDATION));
		result.add(new ServletInfo(authenticationGuiUserCreateServlet, AuthenticationGuiConstants.URL_USER_CREATE));
		result.add(new ServletInfo(authenticationGuiUserDeleteServlet, AuthenticationGuiConstants.URL_USER_DELETE));
		result.add(new ServletInfo(authenticationGuiChangePasswordServlet, AuthenticationGuiConstants.URL_USER_PASSWORD_CHANGE));
		result.add(new ServletInfo(authenticationGuiUserPasswordLostServlet, AuthenticationGuiConstants.URL_USER_PASSWORD_LOST_EMAIL));
		result.add(new ServletInfo(authenticationGuiUserPasswordLostResetServlet, AuthenticationGuiConstants.URL_USER_PASSWORD_LOST_RESET));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		for (final ConfigurationDescription configuration : authenticationGuiConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		result.add(new ServiceInfo(NavigationEntry.class, authenticationGuiNavigationEntry));
		return result;
	}

}
