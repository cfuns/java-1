package de.benjaminborbe.index.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.index.IndexConstants;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class IndexConfigImpl extends ConfigurationBase implements IndexConfig {

	private final ConfigurationDescriptionBoolean indexDistributedEnabled = new ConfigurationDescriptionBoolean(false, IndexConstants.INDEX_DISTRIBUTED_ENABLED,
			"Index Distributed Enabled");

	private final ConfigurationDescriptionBoolean indexLuceneEnabled = new ConfigurationDescriptionBoolean(true, IndexConstants.INDEX_LUCENE_ENABLED, "Index Lucene Enabled");

	@Inject
	public IndexConfigImpl(final Logger logger, final ConfigurationService configurationService, final ParseUtil parseUtil) {
		super(logger, configurationService, parseUtil);
	}

	@Override
	public Collection<ConfigurationDescription> getConfigurations() {
		final Set<ConfigurationDescription> result = new HashSet<ConfigurationDescription>();
		result.add(indexDistributedEnabled);
		result.add(indexLuceneEnabled);
		return result;
	}

	@Override
	public boolean isIndexDistributedEnabled() {
		return getValueBoolean(indexDistributedEnabled);
	}

	@Override
	public boolean isIndexLuceneEnabled() {
		return getValueBoolean(indexLuceneEnabled);
	}
}
