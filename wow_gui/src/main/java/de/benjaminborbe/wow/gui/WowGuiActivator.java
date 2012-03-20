package de.benjaminborbe.wow.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.wow.gui.guice.WowGuiModules;
import de.benjaminborbe.wow.gui.service.WowGuiSpecialSearchBuffed;
import de.benjaminborbe.wow.gui.service.WowGuiSpecialSearchThottbot;
import de.benjaminborbe.wow.gui.service.WowGuiSpecialSearchWowhead;
import de.benjaminborbe.wow.gui.servlet.WowGuiServlet;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class WowGuiActivator extends HttpBundleActivator {

	@Inject
	private WowGuiServlet wowGuiServlet;

	@Inject
	private WowGuiSpecialSearchWowhead specialSearchWowhead;

	@Inject
	private WowGuiSpecialSearchThottbot specialSearchThottbot;

	@Inject
	private WowGuiSpecialSearchBuffed specialSearchBuffed;

	public WowGuiActivator() {
		super("wow");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WowGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(wowGuiServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(SearchSpecial.class, specialSearchWowhead, specialSearchWowhead.getClass().getName()));
		result.add(new ServiceInfo(SearchSpecial.class, specialSearchThottbot, specialSearchThottbot.getClass().getName()));
		result.add(new ServiceInfo(SearchSpecial.class, specialSearchBuffed, specialSearchBuffed.getClass().getName()));
		return result;
	}
}
