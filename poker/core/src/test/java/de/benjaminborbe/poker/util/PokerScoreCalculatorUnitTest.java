package de.benjaminborbe.poker.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class PokerScoreCalculatorUnitTest {

	@Test
	public void testEmpty() throws Exception {
		final PokerScoreCalculator pokerScoreCalculator = new PokerScoreCalculator();
		final int playerAmount = 0;
		assertThat(pokerScoreCalculator.calcScores(playerAmount), is(notNullValue()));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).size(), is(playerAmount));
	}

	@Test
	public void testOne() throws Exception {
		final PokerScoreCalculator pokerScoreCalculator = new PokerScoreCalculator();
		final int playerAmount = 1;
		assertThat(pokerScoreCalculator.calcScores(playerAmount), is(notNullValue()));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).size(), is(playerAmount));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(0), is(playerAmount));
	}

	@Test
	public void testTwo() throws Exception {
		final PokerScoreCalculator pokerScoreCalculator = new PokerScoreCalculator();
		final int playerAmount = 2;
		assertThat(pokerScoreCalculator.calcScores(playerAmount), is(notNullValue()));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).size(), is(playerAmount));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(0), is(playerAmount));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(1), is(0));
	}

	@Test
	public void testThree() throws Exception {
		final PokerScoreCalculator pokerScoreCalculator = new PokerScoreCalculator();
		final int playerAmount = 3;
		assertThat(pokerScoreCalculator.calcScores(playerAmount), is(notNullValue()));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).size(), is(playerAmount));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(0), is(playerAmount));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(1), is(0));
		assertThat(pokerScoreCalculator.calcScores(playerAmount).get(2), is(0));
	}

}
