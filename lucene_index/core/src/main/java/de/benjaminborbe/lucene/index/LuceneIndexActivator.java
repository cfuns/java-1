package de.benjaminborbe.lucene.index;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.lucene.index.api.LuceneIndexService;
import de.benjaminborbe.lucene.index.config.LuceneIndexConfig;
import de.benjaminborbe.lucene.index.guice.LuceneIndexModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.util.NamedSPILoader;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LuceneIndexActivator extends BaseBundleActivator {

	@Inject
	private LuceneIndexService indexerService;

	@Inject
	private LuceneIndexConfig indexConfig;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LuceneIndexModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(LuceneIndexService.class, indexerService));
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
			} catch (final ClassNotFoundException e) {
			}
			final NamedSPILoader<Codec> loader = new NamedSPILoader<>(Codec.class);
			Codec.setDefault(loader.lookup("Lucene40"));
			Codec.getDefault();
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
	}
}
