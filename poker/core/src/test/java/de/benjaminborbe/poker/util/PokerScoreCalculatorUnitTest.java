package de.benjaminborbe.poker.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class PokerScoreCalculatorUnitTest {

	@Test
	public void testEmpty() throws Exception {
		PokerScoreCalculator pokerScoreCalculator = new PokerScoreCalculator();
		int playerAmount = 0;
		assertThat(pokerScoreCalculator.calcScores(playerAmount), is(notNullValue()));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).size(), is(playerAmount));
	}

	@Test
	public void testOne() throws Exception {
		PokerScoreCalculator pokerScoreCalculator = new PokerScoreCalculator();
		int playerAmount = 1;
		assertThat(pokerScoreCalculator.calcScores(playerAmount), is(notNullValue()));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).size(), is(playerAmount));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(0), is(1));
	}

	@Test
	public void testTwo() throws Exception {
		PokerScoreCalculator pokerScoreCalculator = new PokerScoreCalculator();
		int playerAmount = 2;
		assertThat(pokerScoreCalculator.calcScores(playerAmount), is(notNullValue()));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).size(), is(playerAmount));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(0), is(1));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(1), is(1));
	}
}
