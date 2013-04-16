package de.benjaminborbe.systemstatus.core.util;

import com.google.inject.Inject;
import de.benjaminborbe.systemstatus.api.SystemstatusPartition;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SystemstatusPartitionUtil {

	private final Logger logger;

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

	@Inject
	public SystemstatusPartitionUtil(final Logger logger) {
		this.logger = logger;
	}

	public Collection<SystemstatusPartition> getPartitions() {
		logger.debug("getPartitions");
		final List<SystemstatusPartition> result = new ArrayList<SystemstatusPartition>();
		for (final File file : File.listRoots()) {
			result.add(new PartitionImpl(file));
		}
		return result;
	}
}
