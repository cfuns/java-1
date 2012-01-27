package de.benjaminborbe.authorization.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.authorization.gui.guice.AuthorizationGuiModules;
import de.benjaminborbe.authorization.gui.servlet.AuthorizationGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class AuthorizationGuiActivator extends HttpBundleActivator {

	@Inject
	private AuthorizationGuiServlet authorizationGuiServlet;

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
		return result;
	}

}
