package de.benjaminborbe.index.util;

import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MathUtil {

	@Inject
	public MathUtil() {
	}

	public Long maxLong(final Collection<Long> numbers) {
		Long result = null;
		for (final Long n : numbers) {
			if (result == null) {
				result = n;
			}
			else {
				result = Math.max(result, n);
			}
		}
		return result;
	}

	public Integer maxInteger(final Collection<Integer> numbers) {
		Integer result = null;
		for (final Integer n : numbers) {
			if (result == null) {
				result = n;
			}
			else {
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
