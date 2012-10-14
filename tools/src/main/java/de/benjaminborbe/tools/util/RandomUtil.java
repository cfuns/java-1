package de.benjaminborbe.tools.util;

import java.util.Random;

import com.google.inject.Inject;

public class RandomUtil {

	private final Random random;

	@Inject
	public RandomUtil(final Random random) {
		this.random = random;
	}

	public int getRandomized(final int orgValue, final int randomPercent) {
		return getRandomized(orgValue, 1d * randomPercent / 100);
	}

	/**
	 * randomFactor = 0 => return orgValue / no random
	 * randomFactor = 1 => return 100% diff
	 */
	public int getRandomized(final int orgValue, final double randomFactor) {
		final int maxDelta = (int) (orgValue * randomFactor);
		final int delta = random.nextInt(2 * maxDelta);
		return orgValue + delta - maxDelta;
	}
}
