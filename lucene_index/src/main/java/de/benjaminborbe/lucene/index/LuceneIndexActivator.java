package de.benjaminborbe.lucene.index;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.util.NamedSPILoader;
import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.lucene.index.api.LuceneIndexSearcherService;
import de.benjaminborbe.lucene.index.api.LuceneIndexerService;
import de.benjaminborbe.lucene.index.config.LuceneIndexConfig;
import de.benjaminborbe.lucene.index.guice.LuceneIndexModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class LuceneIndexActivator extends BaseBundleActivator {

	@Inject
	private LuceneIndexerService indexerService;

	@Inject
	private LuceneIndexSearcherService indexSearcherService;

	@Inject
	private LuceneIndexConfig indexConfig;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LuceneIndexModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(LuceneIndexerService.class, indexerService));
		result.add(new ServiceInfo(LuceneIndexSearcherService.class, indexSearcherService));
		for (final ConfigurationDescription configuration : indexConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

	static {
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(LuceneIndexActivator.class.getClassLoader());
			// final org.apache.lucene.codecs.lucene40.Lucene40Codec codec;
			try {
				Class.forName("org.apache.lucene.codecs.lucene40.Lucene40Codec");
			}
			catch (final ClassNotFoundException e) {
			}
			final NamedSPILoader<Codec> loader = new NamedSPILoader<Codec>(Codec.class);
			Codec.setDefault(loader.lookup("Lucene40"));
			Codec.getDefault();
		}
		finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
	}
}
