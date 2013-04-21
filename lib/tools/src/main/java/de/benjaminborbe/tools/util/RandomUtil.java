package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import java.util.Random;

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
		final int delta = randomInt(2 * maxDelta);
		return orgValue + delta - maxDelta;
	}

	private int randomInt(final int maxDelta) {
		if (maxDelta == 0) {
			return 0;
		}
		if (maxDelta > 0) {
			return random.nextInt(maxDelta);
		} else {
			return random.nextInt(maxDelta * -1) * -1;
		}
	}

	public int getRandomInt(int minValue, int maxValue) {
		return minValue + random.nextInt(maxValue - minValue);
	}
}
