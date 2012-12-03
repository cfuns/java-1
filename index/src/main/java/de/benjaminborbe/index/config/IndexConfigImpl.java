package de.benjaminborbe.index.config;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.index.IndexConstants;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class IndexConfigImpl extends ConfigurationBase implements IndexConfig {

	private final ConfigurationDescriptionString indexDirectory = new ConfigurationDescriptionString(getTempDir().getAbsolutePath(), IndexConstants.CONFIG_INDEX_DIR,
			"Index directory");

	@Inject
	public IndexConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(indexDirectory);
		return result;
	}

	@Override
	public String getIndexDirectory() {
		return getValueString(indexDirectory);
	}

	private File getTempDir() {
		final String property = "java.io.tmpdir";
		final File tmpDir = new File(System.getProperty(property));
		if (tmpDir.isDirectory() && tmpDir.canWrite()) {
			return tmpDir;
		}
		else {
			return new File("/tmp");
		}
	}
}
