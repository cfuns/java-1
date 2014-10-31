package de.benjaminborbe.poker.table.server.servlet;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.model.Leaderboard;
import de.benjaminborbe.poker.table.client.service.StatusService;
import de.benjaminborbe.poker.table.server.mapper.MapServiceValuesToGui;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class PokerTableStatusServlet extends RemoteServiceServlet implements StatusService {

    private static final long serialVersionUID = 626602494842274438L;

    private final Logger logger;

    private final MapServiceValuesToGui mapServiceValuesToGui;

    private final PokerService pokerService;

    @Inject
    public PokerTableStatusServlet(
            final Logger logger,
            final MapServiceValuesToGui mapServiceValuesToGui,
            final PokerService pokerService) {
        this.logger = logger;
        this.mapServiceValuesToGui = mapServiceValuesToGui;
        this.pokerService = pokerService;
    }

    @Override
    public void log(final String message, final Throwable t) {
        logger.debug(message, t);
    }

    @Override
    public void log(final String msg) {
        logger.debug(msg);
    }

    @Override
    public String processCall(final String payload) throws SerializationException {
        logger.debug("processCall request: " + payload);
        try {
            final String result = super.processCall(payload);
            logger.debug("processCall result: " + result);
            return result;
        } catch (RuntimeException e) {
            logger.debug(e.getClass().getName(), e);
            throw e;
        } catch (SerializationException e) {
            logger.debug(e.getClass().getName(), e);
            throw e;
        }
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("service start");
        final Thread currentThread = Thread.currentThread();
        final ClassLoader oldContextClassLoader = currentThread.getContextClassLoader();
        try {
            currentThread.setContextClassLoader(this.getClass().getClassLoader());
            super.service(req, resp);
        } catch (Exception e) {
            logger.debug(e.getClass().getName(), e);
            throw new ServletException(e);
        } finally {
            currentThread.setContextClassLoader(oldContextClassLoader);
        }
        logger.debug("service end");
    }

    @Override
    public Game getGame(final String gameId) {
        logger.debug("getGame");
        try {
            final PokerGameIdentifier pokerGameIdentifier = pokerService.createGameIdentifier(gameId);
            PokerGame pokerGame = pokerService.getGame(pokerGameIdentifier);
            return mapServiceValuesToGui.mapPokerGameToGame(pokerGame, pokerGameIdentifier);
        } catch (PokerServiceException e) {
            logger.debug("poker service error");
            return new Game(e.getLocalizedMessage());
        }
    }

    @Override
    public Leaderboard getAllPlayers() {
        logger.debug("getPlayers");
        try {
            final Collection<PokerPlayer> pokerPlayers = pokerService.getPlayers();
            return mapServiceValuesToGui.mapPlayersToLeaderboard(pokerPlayers);
        } catch (PokerServiceException e) {
            logger.debug("poker service error");
            return new Leaderboard(e.getLocalizedMessage());
        }
    }

}
