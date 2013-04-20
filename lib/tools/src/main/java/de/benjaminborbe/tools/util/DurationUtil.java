package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import com.google.inject.Provider;
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
