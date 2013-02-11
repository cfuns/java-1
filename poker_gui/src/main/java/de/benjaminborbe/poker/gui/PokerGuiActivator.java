package de.benjaminborbe.poker.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.poker.gui.guice.PokerGuiModules;
import de.benjaminborbe.poker.gui.service.PokerGuiNavigationEntry;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameCreateServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameListServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameViewServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiPlayerListServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiPlayerViewServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class PokerGuiActivator extends HttpBundleActivator {

	@Inject
	private PokerGuiPlayerListServlet pokerGuiPlayerListServlet;

	@Inject
	private PokerGuiPlayerViewServlet pokerGuiPlayerViewServlet;

	@Inject
	private PokerGuiServlet pokerGuiServlet;

	@Inject
	private PokerGuiNavigationEntry pokerGuiNavigationEntry;

	@Inject
	private PokerGuiGameListServlet pokerGuiGameListServlet;

	@Inject
	private PokerGuiGameCreateServlet pokerGuiGameCreateServlet;

	@Inject
	private PokerGuiGameViewServlet pokerGuiGameViewServlet;

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
		result.add(new ServletInfo(pokerGuiGameCreateServlet, PokerGuiConstants.URL_GAME_CREATE));
		result.add(new ServletInfo(pokerGuiGameListServlet, PokerGuiConstants.URL_GAME_LIST));
		result.add(new ServletInfo(pokerGuiGameViewServlet, PokerGuiConstants.URL_GAME_VIEW));
		result.add(new ServletInfo(pokerGuiServlet, PokerGuiConstants.URL_HOME));
		result.add(new ServletInfo(pokerGuiPlayerListServlet, PokerGuiConstants.URL_PLAYER_LIST));
		result.add(new ServletInfo(pokerGuiPlayerViewServlet, PokerGuiConstants.URL_PLAYER_VIEW));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, pokerGuiNavigationEntry));
		return result;
	}

}
