package de.benjaminborbe.tools.util;

import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;

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
