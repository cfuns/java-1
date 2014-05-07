package de.benjaminborbe.poker.table.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import de.benjaminborbe.poker.table.client.gin.PokerTableClientGinjector;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.service.StatusService;
import de.benjaminborbe.poker.table.client.service.StatusServiceAsync;
import de.benjaminborbe.poker.table.client.service.StatusServiceCallBack;
import de.benjaminborbe.poker.table.client.ui.DataSource;
import de.benjaminborbe.poker.table.client.ui.PokerCanvas;

public class Home implements EntryPoint {

	private static final String SERVER_ERROR = "An error occurred while "
		+ "attempting to contact the server. Please check your network "
		+ "connection and try again.";

	private final PokerTableClientGinjector injector = GWT.create(PokerTableClientGinjector.class);

	private int frequenz = 500;

	private PokerCanvas pokerCanvas;

	private int frameWidth = Window.getClientWidth();

	private int frameHeight = Window.getClientHeight();

	private DataSource dataSource = new DataSource();

	@Override
	public void onModuleLoad() {
		// onModuleLoadTest();
		onModuleLoadGui();
	}

	public void onModuleLoadTest() {
		final Widget panel = injector.getMainPanel();
		RootPanel.get().add(panel);
	}

	public void onModuleLoadGui() {
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth(frameWidth + "px");
		hPanel.setWidth(frameHeight + "px");
		hPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		pokerCanvas = new PokerCanvas(frameWidth, frameHeight);
		hPanel.add(pokerCanvas);
		RootPanel.get().add(hPanel);

		startStatusServiceTimer();
	}

	private void loadStatus() {
		StatusServiceAsync service = GWT.create(StatusService.class);
		ServiceDefTarget serviceDef = (ServiceDefTarget) service;
		serviceDef.setServiceEntryPoint(GWT.getModuleBaseURL() + "statusService");
		StatusServiceCallBack statusServiceCallBack = new StatusServiceCallBack(dataSource);
		service.getGames(statusServiceCallBack);
	}

	private void startStatusServiceTimer() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				loadStatus();
				updateCanvas();
			}
		};
		timer.scheduleRepeating(frequenz);
	}

	private void updateCanvas() {
		if (dataSource.getGames().size() > 0) {
			for (Game game : dataSource.getGames()) {
				pokerCanvas.setRunFlag(game.isGameRunning());
				pokerCanvas.updateGameText(game);
				pokerCanvas.updatePlayerPot();
				pokerCanvas.updateActivePlayer();
				pokerCanvas.updatePot(game);
				pokerCanvas.updateBoardCards(game.getBoardCards());
				pokerCanvas.updatePlayerPositions(game.getPlayers());
				pokerCanvas.updateWinner();
			}
		}
	}
}
