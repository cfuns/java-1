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
		if (playerAmount > 0) {
			result.add(playerAmount);
			for (int i = 1; i < playerAmount; ++i) {
				result.add(0);
			}
		}
		return result;
	}
}
