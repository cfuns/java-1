package de.benjaminborbe.xmpp.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.xmpp.gui.guice.XmppGuiModules;
import de.benjaminborbe.xmpp.gui.servlet.XmppGuiServlet;

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

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(xmppFilter, ".*", 998));
	// return result;
	// }

	// @Override
	// protected Collection<ResourceInfo> getResouceInfos() {
	// final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
	// // result.add(new ResourceInfo("/css", "css"));
	// // result.add(new ResourceInfo("/js", "js"));
	// // result.add(new ResourceInfo("/images", "images"));
	// return result;
	// }
}
