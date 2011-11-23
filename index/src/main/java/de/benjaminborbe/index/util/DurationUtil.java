package de.benjaminborbe.index.util;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class DurationUtil {

	private final Provider<Duration> durationProvider;

	@Inject
	public DurationUtil(final Provider<Duration> durationProvider) {
		this.durationProvider = durationProvider;
	}

	public Duration getDuration() {
		return durationProvider.get();
	}
}
