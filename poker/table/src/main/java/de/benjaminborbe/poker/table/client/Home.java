package de.benjaminborbe.poker.table.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.model.Leaderboard;
import de.benjaminborbe.poker.table.client.service.GameStatusServiceCallBack;
import de.benjaminborbe.poker.table.client.service.LeaderboardStatusServiceCallBack;
import de.benjaminborbe.poker.table.client.service.StatusService;
import de.benjaminborbe.poker.table.client.service.StatusServiceAsync;
import de.benjaminborbe.poker.table.client.ui.GameDataSource;
import de.benjaminborbe.poker.table.client.ui.LeaderboardCanvas;
import de.benjaminborbe.poker.table.client.ui.LeaderboardDataSource;
import de.benjaminborbe.poker.table.client.ui.PokerCanvas;

import java.util.ArrayList;

public class Home implements EntryPoint {

	private final int frequenz_in_ms = 500;

	private final int frameWidth = Window.getClientWidth();

	private final int frameHeight = Window.getClientHeight();

	private final LeaderboardDataSource leaderboardDataSource = new LeaderboardDataSource();

	private final GameDataSource gameDataSource = new GameDataSource();

	private final ArrayList<String> errorList = new ArrayList<String>();

	private PokerCanvas pokerCanvas;

	private LeaderboardCanvas leaderboardCanvas;

	@Override
	public void onModuleLoad() {
		onModuleLoadGui();
	}

	public void onModuleLoadGui() {
		RootPanel.get().add(createMainPanel());
		startStatusServiceTimer();
	}

	private HorizontalPanel createMainPanel() {
		final HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth(frameWidth + "px");
		hPanel.setHeight(frameHeight + "px");
		hPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		hPanel.add(createLeaderboardPanel());
		hPanel.add(createPokerTablePanel());
		return hPanel;
	}

	private HorizontalPanel createPokerTablePanel() {
		int pokerPanelWidth = (int) (frameWidth * 0.9);
		pokerCanvas = new PokerCanvas(pokerPanelWidth, frameHeight);
		final HorizontalPanel pokerPanel = new HorizontalPanel();
		pokerPanel.setWidth(pokerPanelWidth + "px");
		pokerPanel.setHeight(frameHeight + "px");
		pokerPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		pokerPanel.add(pokerCanvas);
		return pokerPanel;
	}

	private HorizontalPanel createLeaderboardPanel() {
		int leaderboardPanelWidth = (int) (frameWidth * 0.1);
		leaderboardCanvas = new LeaderboardCanvas(leaderboardPanelWidth, frameHeight);
		final HorizontalPanel leaderboardPanel = new HorizontalPanel();
		leaderboardPanel.setWidth(leaderboardPanelWidth + "px");
		leaderboardPanel.setHeight(frameHeight + "px");
		leaderboardPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		leaderboardPanel.add(leaderboardCanvas);
		return leaderboardPanel;
	}

	private void loadStatus() {
		final StatusServiceAsync service = GWT.create(StatusService.class);
		final ServiceDefTarget serviceDef = (ServiceDefTarget) service;
		serviceDef.setServiceEntryPoint(GWT.getModuleBaseURL() + "statusService");
		final GameStatusServiceCallBack gameStatusServiceCallBack = new GameStatusServiceCallBack(gameDataSource);
		final LeaderboardStatusServiceCallBack statusServiceCallBackLeaderboard = new LeaderboardStatusServiceCallBack(leaderboardDataSource);
		final String gameId = Window.Location.getParameter("game_id");
		service.getGame(gameId, gameStatusServiceCallBack);
		service.getAllPlayers(statusServiceCallBackLeaderboard);
	}

	private void startStatusServiceTimer() {
		final Timer timer = new Timer() {

			@Override
			public void run() {
				loadStatus();
				updateCanvas();
			}
		};
		timer.scheduleRepeating(frequenz_in_ms);
	}

	private void updateCanvas() {
		Game game = null;
		Leaderboard leaderboard = null;
		if (gameDataSource.getGame() != null) {
			game = gameDataSource.getGame();
			pokerCanvas.setRunFlag(game.isGameRunning());
			pokerCanvas.updateGameText(game);
			pokerCanvas.updatePlayerPot();
			pokerCanvas.updateActivePlayer();
			pokerCanvas.updatePot(game);
			pokerCanvas.updateBoardCards(game.getBoardCards());
			pokerCanvas.updatePlayerPositions(game.getPlayers());
			pokerCanvas.updateWinner();
		}
		if (leaderboardDataSource.getLeaderboard() != null) {
			leaderboard = leaderboardDataSource.getLeaderboard();
			leaderboardCanvas.updateLeaderboard(leaderboard.getSortPlayersByScore());
		}

		if (game != null && leaderboard != null) {
			errorList.clear();
			pokerCanvas.updateErrorText(getErrorList(game, leaderboard));
		}
	}

	private ArrayList<String> getErrorList(Game game, Leaderboard leaderboard) {
		errorList.add(game.getPokerServiceException());
		errorList.add(leaderboard.getPokerServiceException());
		return errorList;
	}

}
