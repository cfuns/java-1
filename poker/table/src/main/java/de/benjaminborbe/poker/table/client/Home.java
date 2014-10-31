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
import de.benjaminborbe.poker.table.client.model.Leaderboard;
import de.benjaminborbe.poker.table.client.service.StatusService;
import de.benjaminborbe.poker.table.client.service.StatusServiceAsync;
import de.benjaminborbe.poker.table.client.service.StatusServiceCallBack;
import de.benjaminborbe.poker.table.client.ui.DataSource;
import de.benjaminborbe.poker.table.client.ui.PokerCanvas;

import java.util.ArrayList;

public class Home implements EntryPoint {

    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    private final PokerTableClientGinjector injector = GWT.create(PokerTableClientGinjector.class);

    private final int frequenz = 100;

    private final int frameWidth = Window.getClientWidth();

    private final int frameHeight = Window.getClientHeight();

    private final DataSource dataSource = new DataSource();

    private PokerCanvas pokerCanvas;

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
        final HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setWidth(frameWidth + "px");
        hPanel.setWidth(frameHeight + "px");
        hPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        pokerCanvas = new PokerCanvas(frameWidth, frameHeight);
        hPanel.add(pokerCanvas);
        RootPanel.get().add(hPanel);

        startStatusServiceTimer();
    }

    private void loadStatus() {
        final StatusServiceAsync service = GWT.create(StatusService.class);
        final ServiceDefTarget serviceDef = (ServiceDefTarget) service;
        serviceDef.setServiceEntryPoint(GWT.getModuleBaseURL() + "statusService");
        final StatusServiceCallBack statusServiceCallBack = new StatusServiceCallBack(dataSource);
        final String gameId = Window.Location.getParameter("game_id");
        service.getGame(gameId, statusServiceCallBack);
    }

    private void startStatusServiceTimer() {
        final Timer timer = new Timer() {

            @Override
            public void run() {
                loadStatus();
                updateCanvas();
            }
        };
        timer.scheduleRepeating(frequenz);
    }

    private void updateCanvas() {
        if (dataSource.getGame() != null) {
            final Game game = dataSource.getGame();
            final Leaderboard leaderboard = dataSource.getLeaderboard();
            pokerCanvas.setRunFlag(game.isGameRunning());
            pokerCanvas.updateGameText(game);
            pokerCanvas.updateErrorText(getErrorList(game, leaderboard));
            pokerCanvas.updatePlayerPot();
            pokerCanvas.updateActivePlayer();
            pokerCanvas.updatePot(game);
            pokerCanvas.updateBoardCards(game.getBoardCards());
            pokerCanvas.updatePlayerPositions(game.getPlayers());
            pokerCanvas.updateWinner();
        }
    }

    private ArrayList<String> getErrorList(Game game, Leaderboard leaderboard) {
        ArrayList<String> errorList = new ArrayList<String>();
        errorList.add(game.getPokerServiceException());
        errorList.add(leaderboard.getPokerServiceException());
        return errorList;
    }

}
