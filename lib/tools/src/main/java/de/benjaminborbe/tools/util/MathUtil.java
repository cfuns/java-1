package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MathUtil {

	@Inject
	public MathUtil() {
	}

	public Long maxLong(final Long... numbers) {
		return maxLong(Arrays.asList(numbers));
	}

	public Long maxLong(final Collection<Long> numbers) {
		Long result = null;
		for (final Long n : numbers) {
			if (result == null) {
				result = n;
			} else if (n != null) {
				result = Math.max(result, n);
			}
		}
		return result;
	}

	public Long minLong(final Long... numbers) {
		return minLong(Arrays.asList(numbers));
	}

	public Long minLong(final Collection<Long> numbers) {
		Long result = null;
		for (final Long n : numbers) {
			if (result == null) {
				result = n;
			} else if (n != null) {
				result = Math.min(result, n);
			}
		}
		return result;
	}

	public Integer minInteger(final Integer... numbers) {
		return minInteger(Arrays.asList(numbers));
	}

	public Integer minInteger(final Collection<Integer> numbers) {
		Integer result = null;
		for (final Integer n : numbers) {
			if (result == null) {
				result = n;
			} else if (n != null) {
				result = Math.min(result, n);
			}
		}
		return result;
	}

	public Integer maxInteger(final Integer... numbers) {
		return maxInteger(Arrays.asList(numbers));
	}

	public Integer maxInteger(final Collection<Integer> numbers) {
		Integer result = null;
		for (final Integer n : numbers) {
			if (result == null) {
				result = n;
			} else if (n != null) {
				result = Math.max(result, n);
			}
		}
		return result;
	}

	public Double avgDouble(final List<Double> values) {
		if (values == null || values.size() == 0) {
			return null;
		}
		return sumDouble(values) / values.size();
	}

	public Double sumDouble(final List<Double> values) {
		Double result = 0d;
		for (final Double value : values) {
			result += value;
		}
		return result;
	}

	public Long avgLong(final List<Long> values) {
		if (values == null || values.size() == 0) {
			return null;
		}
		return sumLong(values) / values.size();
	}

	public Long sumLong(final List<Long> values) {
		Long result = 0l;
		for (final Long value : values) {
			result += value;
		}
		return result;
	}
}
