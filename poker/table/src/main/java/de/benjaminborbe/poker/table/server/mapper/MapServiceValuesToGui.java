package de.benjaminborbe.poker.table.server.mapper;

import com.google.inject.Inject;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.table.client.model.Card;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.model.Leaderboard;
import de.benjaminborbe.poker.table.client.model.Player;

import java.util.Collection;

public class MapServiceValuesToGui {

    private final PokerService pokerService;

    @Inject
    public MapServiceValuesToGui(final PokerService pokerService) {
        this.pokerService = pokerService;
    }

    public Leaderboard mapPlayersToLeaderboard(final Collection<PokerPlayer> pokerPlayers) {
        final Leaderboard leaderboard = new Leaderboard();
        for (final PokerPlayer pokerPlayer : pokerPlayers) {
            Player player = new Player();
            player.setUsername(pokerPlayer.getName());
            player.setScore(pokerPlayer.getScore().intValue());
            leaderboard.getPlayers().add(player);
        }
        return leaderboard;
    }

    public Game mapPokerGameToGame(final PokerGame pokerGame, final PokerGameIdentifier pokerGameIdentifier) throws PokerServiceException {
        final Game game = new Game();
        game.setGameRound(pokerGame.getRound().toString());
        game.setGameSmallBlind(pokerGame.getSmallBlind().toString());
        game.setGamePot(pokerGame.getPot().toString());
        game.setGameBigBlind(pokerGame.getBigBlind().toString());

        game.setGameId(pokerGame.getId().getId());
        game.setGameRunning(pokerGame.getRunning());
        game.setGameBid(pokerGame.getBet().toString());
        game.setGameMaxBid(pokerGame.getMaxBid().toString());
        game.setGameName(pokerGame.getName());

        mapBoardCardsToGame(game, pokerGameIdentifier);
        mapPlayersToGame(game, pokerGameIdentifier);

        return game;
    }

    private void mapBoardCardsToGame(final Game game, final PokerGameIdentifier pokerGameIdentifier) throws PokerServiceException {
        final Collection<PokerCardIdentifier> boardCards = pokerService.getBoardCards(pokerGameIdentifier);
        for (final PokerCardIdentifier pokerCardIdentifier : boardCards) {
            final Card card = new Card();
            card.setCard(pokerCardIdentifier.getId());
            game.addCard(card);
        }
    }

    private void mapHandCardsToPlayer(final Player player, final PokerPlayerIdentifier pokerPlayerIdentifier) throws PokerServiceException {
        final Collection<PokerCardIdentifier> handCards = pokerService.getHandCards(pokerPlayerIdentifier);
        for (final PokerCardIdentifier pokerHandCardIdentifier : handCards) {
            final Card card = new Card();
            card.setCard(pokerHandCardIdentifier.getId());
            player.addCard(card);
        }
    }

    private void mapPlayersToGame(final Game game, final PokerGameIdentifier pokerGameIdentifier) throws PokerServiceException {
        final PokerPlayer activePlayer = getActivePokerPlayer(pokerGameIdentifier);
        final Collection<PokerPlayerIdentifier> players = pokerService.getPlayers(pokerGameIdentifier);
        for (final PokerPlayerIdentifier pokerPlayerIdentifier : players) {
            final PokerPlayer currentPlayer = getCurrentPlayer(pokerPlayerIdentifier);
            final Player player = new Player();
            player.setUsername(currentPlayer.getName());
            player.setId(currentPlayer.getId().getId());
            player.setCredits(currentPlayer.getAmount().intValue());
            if (activePlayer.getId().getId().equals(player.getId())) {
                game.setActivePlayer(player);
                player.setActivePlayer(true);
            }
            mapHandCardsToPlayer(player, pokerPlayerIdentifier);
            game.addPlayer(player);
        }
    }

    private PokerPlayer getActivePokerPlayer(final PokerGameIdentifier pokerGameIdentifier) throws PokerServiceException {
        final PokerPlayerIdentifier activePlayerIdentifier = pokerService.getActivePlayer(pokerGameIdentifier);
        final PokerPlayer activePlayer = pokerService.getPlayer(activePlayerIdentifier);
        return activePlayer;
    }

    private PokerPlayer getCurrentPlayer(final PokerPlayerIdentifier pokerPlayerIdentifier) throws PokerServiceException {
        return pokerService.getPlayer(pokerPlayerIdentifier);
    }

}
