package de.benjaminborbe.dns.gui;

import de.benjaminborbe.dns.gui.guice.DnsGuiModules;
import de.benjaminborbe.dns.gui.servlet.DnsGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DnsGuiActivator extends HttpBundleActivator {

	@Inject
	private DnsGuiServlet dnsGuiServlet;

	public DnsGuiActivator() {
		super(DnsGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DnsGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(dnsGuiServlet, DnsGuiConstants.URL_HOME));
		return result;
	}

}
