package de.benjaminborbe.systemstatus.service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import de.benjaminborbe.systemstatus.api.SystemstatusPartition;
import de.benjaminborbe.systemstatus.api.SystemstatusService;

@Singleton
public class SystemstatusServiceImpl implements SystemstatusService {

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

	private final class PartitionImpl implements SystemstatusPartition {

		private final File file;

		private PartitionImpl(final File file) {
			this.file = file;
		}

		@Override
		public String getAbsolutePath() {
			return file.getAbsolutePath();
		}

		@Override
		public long getFreeSpace() {
			return file.getFreeSpace();
		}

		@Override
		public long getUsableSpace() {
			return file.getUsableSpace();
		}

		@Override
		public long getTotalSpace() {
			return file.getTotalSpace();
		}

		@Override
		public long getUsedSpace() {
			return file.getTotalSpace() - file.getFreeSpace();
		}
	}

	private final Logger logger;

	@Inject
	public SystemstatusServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Collection<SystemstatusPartition> getPartitions() {
		logger.debug("getPartitions");
		final List<SystemstatusPartition> result = new ArrayList<SystemstatusPartition>();
		for (final File file : File.listRoots()) {
			result.add(new PartitionImpl(file));
		}
		return result;
	}

	@Override
	public SystemstatusMemoryUsage getMemoryUsage() {
		logger.debug("getMemoryUsage");
		final MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
		final MemoryUsage heap = mem.getHeapMemoryUsage();
		final MemoryUsage nonHeap = mem.getNonHeapMemoryUsage();

		return new SystemstatusMemoryUsageImpl(nonHeap, heap);
	}
}
