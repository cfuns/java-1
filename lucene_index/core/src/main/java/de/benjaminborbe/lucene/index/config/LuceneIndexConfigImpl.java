package de.benjaminborbe.lucene.index.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionString;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.lucene.index.LuceneIndexConstants;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class LuceneIndexConfigImpl extends ConfigurationBase implements LuceneIndexConfig {

	private final ConfigurationDescriptionString indexDirectory = new ConfigurationDescriptionString(getTempDir().getAbsolutePath(), LuceneIndexConstants.CONFIG_INDEX_DIR,
		"LuceneIndex directory");

	@Inject
	public LuceneIndexConfigImpl(
		final Logger logger,
		final ConfigurationService configurationService,
		final ParseUtil parseUtil,
		final ConfigurationServiceCache configurationServiceCache
	) {
		super(logger, parseUtil, configurationServiceCache);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(indexDirectory);
		return result;
	}

	@Override
	public String getLuceneIndexDirectory() {
		return getValueString(indexDirectory);
	}

	private File getTempDir() {
		final String property = "java.io.tmpdir";
		final File tmpDir = new File(System.getProperty(property));
		if (tmpDir.isDirectory() && tmpDir.canWrite()) {
			return tmpDir;
		} else {
			return new File("/tmp");
		}
	}
}
