package de.benjaminborbe.poker.service;

import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.game.PokerGameBean;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class PokerServiceImplUnitTest {

	@Test
	public void testGetActivePlayerNoPlayer() throws Exception {
		final PokerServiceImpl pokerService = new PokerServiceImpl(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		final PokerGameBean gameBean = new PokerGameBean();
		final PokerPlayerIdentifier activePlayer = pokerService.getActivePlayer(gameBean);
		assertThat(activePlayer, is(nullValue()));
	}

	@Test
	public void testGetActivePlayer() throws Exception {
		final PokerServiceImpl pokerService = new PokerServiceImpl(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		{
			final PokerGameBean gameBean = new PokerGameBean();
			gameBean.setActivePlayers(Arrays.asList(new PokerPlayerIdentifier("a"), new PokerPlayerIdentifier("b"), new PokerPlayerIdentifier("c")));
			gameBean.setActivePosition(0);
			final PokerPlayerIdentifier activePlayer = pokerService.getActivePlayer(gameBean);
			assertThat(activePlayer, is(new PokerPlayerIdentifier("a")));
		}
		{
			final PokerGameBean gameBean = new PokerGameBean();
			gameBean.setActivePlayers(Arrays.asList(new PokerPlayerIdentifier("a"), new PokerPlayerIdentifier("b"), new PokerPlayerIdentifier("c")));
			gameBean.setActivePosition(1);
			final PokerPlayerIdentifier activePlayer = pokerService.getActivePlayer(gameBean);
			assertThat(activePlayer, is(new PokerPlayerIdentifier("b")));
		}
		{
			final PokerGameBean gameBean = new PokerGameBean();
			gameBean.setActivePlayers(Arrays.asList(new PokerPlayerIdentifier("a"), new PokerPlayerIdentifier("b"), new PokerPlayerIdentifier("c")));
			gameBean.setActivePosition(2);
			final PokerPlayerIdentifier activePlayer = pokerService.getActivePlayer(gameBean);
			assertThat(activePlayer, is(new PokerPlayerIdentifier("c")));
		}
	}

	@Test
	public void testGetActivePlayerOutOfRange() throws Exception {
		final PokerServiceImpl pokerService = new PokerServiceImpl(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
		final PokerGameBean gameBean = new PokerGameBean();
		gameBean.setActivePlayers(Arrays.asList(new PokerPlayerIdentifier("a"), new PokerPlayerIdentifier("b"), new PokerPlayerIdentifier("c")));
		gameBean.setActivePosition(4);
		final PokerPlayerIdentifier activePlayer = pokerService.getActivePlayer(gameBean);
		assertThat(activePlayer, is(nullValue()));
	}
}
