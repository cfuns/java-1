package de.benjaminborbe.poker.util;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PokerScoreCalculator {

	@Inject
	public PokerScoreCalculator() {
	}

	public List<Integer> calcScores(final int playerAmount) {
		final List<Integer> result = new ArrayList<Integer>(playerAmount);
		for (int i = 0; i < playerAmount; ++i) {
			result.add(playerAmount);
		}
		return result;
	}
}
