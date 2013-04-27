package de.benjaminborbe.systemstatus.core.util;

import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class SystemstatusMemoryUtil {

	private final class SystemstatusMemoryUsageImpl implements SystemstatusMemoryUsage {

		private final MemoryUsage nonHeap;

		private final MemoryUsage heap;

		private SystemstatusMemoryUsageImpl(final MemoryUsage nonHeap, final MemoryUsage heap) {
			this.nonHeap = nonHeap;
			this.heap = heap;
		}

		@Override
		public long getNonHeapMax() {
			return nonHeap.getMax();
		}

		@Override
		public long getNonHeapUsed() {
			return nonHeap.getUsed();
		}

		@Override
		public long getHeapMax() {
			return heap.getMax();
		}

		@Override
		public long getHeapUsed() {
			return heap.getUsed();
		}
	}

	private final Logger logger;

	@Inject
	public SystemstatusMemoryUtil(final Logger logger) {
		this.logger = logger;
	}

	public SystemstatusMemoryUsage getMemoryUsage() {
		logger.debug("getMemoryUsage");
		final MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
		final MemoryUsage heap = mem.getHeapMemoryUsage();
		final MemoryUsage nonHeap = mem.getNonHeapMemoryUsage();

		return new SystemstatusMemoryUsageImpl(nonHeap, heap);
	}
}
