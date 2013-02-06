package de.benjaminborbe.poker.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.poker.gui.guice.PokerGuiModules;
import de.benjaminborbe.poker.gui.servlet.PokerGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class PokerGuiActivator extends HttpBundleActivator {

	@Inject
	private PokerGuiServlet pokerGuiServlet;

	public PokerGuiActivator() {
		super(PokerGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PokerGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(pokerGuiServlet, PokerGuiConstants.URL_HOME));
		return result;
	}

}
