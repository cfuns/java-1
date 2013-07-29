package de.benjaminborbe.xmlrpc.gui;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.xmlrpc.gui.guice.XmlrpcGuiModules;
import de.benjaminborbe.xmlrpc.gui.servlet.XmlrpcGuiServlet;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class XmlrpcGuiActivator extends HttpBundleActivator {

	@Inject
	private XmlrpcGuiServlet xmlrpcGuiServlet;

	public XmlrpcGuiActivator() {
		super(XmlrpcGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new XmlrpcGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(xmlrpcGuiServlet, XmlrpcGuiConstants.URL_HOME));
		return result;
	}

}
