package de.benjaminborbe.poker.gui;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.poker.gui.config.PokerGuiConfig;
import de.benjaminborbe.poker.gui.guice.PokerGuiModules;
import de.benjaminborbe.poker.gui.service.PokerGuiNavigationEntry;
import de.benjaminborbe.poker.gui.servlet.PokerGuiActionCallJsonServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiActionCallServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiActionFoldJsonServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiActionFoldServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiActionRaiseJsonServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiActionRaiseServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiApiHelpServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiDashboardStatusJsonServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameCreateServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameDeleteServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameJoinServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameLeaveServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameListServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameStartServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameStatusJsonServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameStopServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameUpdateServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiGameViewServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiPlayerCreateServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiPlayerDeleteServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiPlayerListServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiPlayerStatusJsonServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiPlayerUpdateServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiPlayerViewServlet;
import de.benjaminborbe.poker.gui.servlet.PokerGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PokerGuiActivator extends HttpBundleActivator {

	@Inject
	private PokerGuiDashboardStatusJsonServlet pokerGuiDashboardStatusJsonServlet;

	@Inject
	private PokerGuiGameStatusJsonServlet pokerGuiGameStatusJsonServlet;

	@Inject
	private PokerGuiPlayerUpdateServlet pokerGuiPlayerUpdateServlet;

	@Inject
	private PokerGuiGameUpdateServlet pokerGuiGameUpdateServlet;

	@Inject
	private PokerGuiConfig pokerGuiConfig;

	@Inject
	private PokerGuiApiHelpServlet pokerGuiApiHelpServlet;

	@Inject
	private PokerGuiActionCallJsonServlet pokerGuiActionCallJsonServlet;

	@Inject
	private PokerGuiActionFoldJsonServlet pokerGuiActionFoldJsonServlet;

	@Inject
	private PokerGuiActionRaiseJsonServlet pokerGuiActionRaiseJsonServlet;

	@Inject
	private PokerGuiPlayerStatusJsonServlet pokerGuiPlayerStatusJsonServlet;

	@Inject
	private PokerGuiActionCallServlet pokerGuiActionCallServlet;

	@Inject
	private PokerGuiActionFoldServlet pokerGuiActionFoldServlet;

	@Inject
	private PokerGuiActionRaiseServlet pokerGuiGameRaiseServlet;

	@Inject
	private PokerGuiGameJoinServlet pokerGuiGameJoinServlet;

	@Inject
	private PokerGuiGameStopServlet pokerGuiGameStopServlet;

	@Inject
	private PokerGuiGameLeaveServlet pokerGuiGameLeaveServlet;

	@Inject
	private PokerGuiPlayerCreateServlet pokerGuiPlayerCreateServlet;

	@Inject
	private PokerGuiGameStartServlet pokerGuiGameStartServlet;

	@Inject
	private PokerGuiPlayerDeleteServlet pokerGuiPlayerDeleteServlet;

	@Inject
	private PokerGuiGameDeleteServlet pokerGuiGameDeleteServlet;

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
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(pokerGuiServlet, PokerGuiConstants.URL_HOME));
		result.add(new ServletInfo(pokerGuiApiHelpServlet, PokerGuiConstants.URL_API_HELP));
		result.add(new ServletInfo(pokerGuiActionCallServlet, PokerGuiConstants.URL_ACTION_CALL));
		result.add(new ServletInfo(pokerGuiActionFoldServlet, PokerGuiConstants.URL_ACTION_FOLD));
		result.add(new ServletInfo(pokerGuiGameRaiseServlet, PokerGuiConstants.URL_ACTION_RAISE));
		result.add(new ServletInfo(pokerGuiGameStopServlet, PokerGuiConstants.URL_GAME_STOP));
		result.add(new ServletInfo(pokerGuiGameJoinServlet, PokerGuiConstants.URL_GAME_JOIN));
		result.add(new ServletInfo(pokerGuiGameLeaveServlet, PokerGuiConstants.URL_GAME_LEAVE));
		result.add(new ServletInfo(pokerGuiGameStartServlet, PokerGuiConstants.URL_GAME_START));
		result.add(new ServletInfo(pokerGuiGameCreateServlet, PokerGuiConstants.URL_GAME_CREATE));
		result.add(new ServletInfo(pokerGuiGameUpdateServlet, PokerGuiConstants.URL_GAME_UPDATE));
		result.add(new ServletInfo(pokerGuiGameDeleteServlet, PokerGuiConstants.URL_GAME_DELETE));
		result.add(new ServletInfo(pokerGuiGameListServlet, PokerGuiConstants.URL_GAME_LIST));
		result.add(new ServletInfo(pokerGuiGameViewServlet, PokerGuiConstants.URL_GAME_VIEW));
		result.add(new ServletInfo(pokerGuiPlayerCreateServlet, PokerGuiConstants.URL_PLAYER_CREATE));
		result.add(new ServletInfo(pokerGuiPlayerUpdateServlet, PokerGuiConstants.URL_PLAYER_UPDATE));
		result.add(new ServletInfo(pokerGuiPlayerListServlet, PokerGuiConstants.URL_PLAYER_LIST));
		result.add(new ServletInfo(pokerGuiPlayerViewServlet, PokerGuiConstants.URL_PLAYER_VIEW));
		result.add(new ServletInfo(pokerGuiPlayerDeleteServlet, PokerGuiConstants.URL_PLAYER_DELETE));
		result.add(new ServletInfo(pokerGuiActionCallJsonServlet, PokerGuiConstants.URL_ACTION_CALL_JSON));
		result.add(new ServletInfo(pokerGuiActionFoldJsonServlet, PokerGuiConstants.URL_ACTION_FOLD_JSON));
		result.add(new ServletInfo(pokerGuiActionRaiseJsonServlet, PokerGuiConstants.URL_ACTION_RAISE_JSON));
		result.add(new ServletInfo(pokerGuiPlayerStatusJsonServlet, PokerGuiConstants.URL_PLAYER_STATUS_JSON));
		result.add(new ServletInfo(pokerGuiGameStatusJsonServlet, PokerGuiConstants.URL_GAME_STATUS_JSON));
		result.add(new ServletInfo(pokerGuiDashboardStatusJsonServlet, PokerGuiConstants.URL_GAME_DASHBOARD_STATUS_JSON));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, pokerGuiNavigationEntry));
		for (final ConfigurationDescription configuration : pokerGuiConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo(PokerGuiConstants.URL_CSS, "css"));
		return result;
	}

}
