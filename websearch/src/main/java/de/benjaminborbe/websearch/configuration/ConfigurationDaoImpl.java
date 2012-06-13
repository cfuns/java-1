package de.benjaminborbe.websearch.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.storage.tools.DaoCacheAutoIncrement;
import de.benjaminborbe.websearch.page.PageDao;

@Singleton
public class ConfigurationDaoImpl extends DaoCacheAutoIncrement<ConfigurationBean, ConfigurationIdentifier> implements ConfigurationDao {

	private final PageDao pageDao;

	@Inject
	public ConfigurationDaoImpl(final Logger logger, final ConfigurationIdGenerator idGenerator, final Provider<ConfigurationBean> provider, final PageDao pageDao) {
		super(logger, idGenerator, provider);
		this.pageDao = pageDao;
		init();
	}

	protected void init() {
		try {
			final URL url = new URL("http://confluence.benjamin-borbe.de/confluence");
			pageDao.findOrCreate(url);

			final ConfigurationBean configuration = create();
			configuration.setUrl(url);
			configuration.setOwnerUsername("bborbe");
			final List<String> excludes = new ArrayList<String>();
			excludes.add("?");
			configuration.setExcludes(excludes);
			save(configuration);
		}
		catch (final Exception e) {
			logger.error(e.getClass().getSimpleName(), e);
		}
	}
}
