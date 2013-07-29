package de.benjaminborbe.xmpp.gui;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.xmpp.gui.guice.XmppGuiModules;
import de.benjaminborbe.xmpp.gui.servlet.XmppGuiServlet;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class XmppGuiActivator extends HttpBundleActivator {

	@Inject
	private XmppGuiServlet xmppGuiServlet;

	public XmppGuiActivator() {
		super(XmppGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new XmppGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(xmppGuiServlet, XmppGuiConstants.URL_HOME));
		return result;
	}

}
