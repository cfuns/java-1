package de.benjaminborbe.index.config;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.tools.ConfigurationBase;
import de.benjaminborbe.configuration.tools.ConfigurationDescriptionBoolean;
import de.benjaminborbe.configuration.tools.ConfigurationServiceCache;
import de.benjaminborbe.index.IndexConstants;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class IndexConfigImpl extends ConfigurationBase implements IndexConfig {

	private final ConfigurationDescriptionBoolean indexDistributedEnabled = new ConfigurationDescriptionBoolean(true, IndexConstants.INDEX_DISTRIBUTED_ENABLED,
		"Index Distributed Enabled");

	private final ConfigurationDescriptionBoolean indexLuceneEnabled = new ConfigurationDescriptionBoolean(false, IndexConstants.INDEX_LUCENE_ENABLED, "Index Lucene Enabled");

	@Inject
	public IndexConfigImpl(
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
