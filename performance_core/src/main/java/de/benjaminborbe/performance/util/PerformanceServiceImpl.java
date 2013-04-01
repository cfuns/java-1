package de.benjaminborbe.performance.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.performance.api.PerformanceEntry;
import de.benjaminborbe.performance.api.PerformanceService;
import de.benjaminborbe.tools.util.ComparatorBase;

@Singleton
public class PerformanceServiceImpl implements PerformanceService {

	private final class PerformanceEntryComparator extends ComparatorBase<PerformanceEntry, Long> {

		@Override
		public Long getValue(final PerformanceEntry o) {
			return new Long(o.getDuration());
		}
	}

	private final Logger logger;

	private final Map<String, Long> data = new HashMap<String, Long>();

	@Inject
	public PerformanceServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void track(final String url, final long duration) {
		logger.trace("track url: " + url + " duration: " + duration);
		data.put(url, duration);
	}

	@Override
	public List<PerformanceEntry> getSlowestEntries(final int limit) {
		final List<PerformanceEntry> entries = buildPerformanceEntries();
		Collections.sort(entries, new PerformanceEntryComparator());
		return entries.subList(0, Math.min(limit, entries.size()));
	}

	protected List<PerformanceEntry> buildPerformanceEntries() {
		final List<PerformanceEntry> result = new ArrayList<PerformanceEntry>();
		for (final Entry<String, Long> e : data.entrySet()) {
			result.add(new PerformanceEntry(e.getKey(), e.getValue()));
		}
		return result;
	}
}
