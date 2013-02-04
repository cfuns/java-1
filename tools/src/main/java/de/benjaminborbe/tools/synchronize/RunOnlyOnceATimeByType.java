package de.benjaminborbe.tools.synchronize;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class RunOnlyOnceATimeByType {

	private final Map<String, RunOnlyOnceATime> data = new HashMap<String, RunOnlyOnceATime>();

	private final Provider<RunOnlyOnceATime> runOnlyOnceATimeProvider;

	@Inject
	public RunOnlyOnceATimeByType(final Provider<RunOnlyOnceATime> runOnlyOnceATimeProvider) {
		this.runOnlyOnceATimeProvider = runOnlyOnceATimeProvider;
	}

	public boolean run(final String type, final Runnable runnable) {
		final RunOnlyOnceATime runOnlyOnceATime = get(type);
		return runOnlyOnceATime.run(runnable);
	}

	private synchronized RunOnlyOnceATime get(final String type) {
		{
			final RunOnlyOnceATime result = data.get(type);
			if (result != null) {
				return result;
			}
		}
		{
			final RunOnlyOnceATime result = runOnlyOnceATimeProvider.get();
			data.put(type, result);
			return result;
		}
	}
}
