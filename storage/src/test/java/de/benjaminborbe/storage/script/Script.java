package de.benjaminborbe.storage.script;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.storage.util.StorageDaoUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class Script {

	public static void main(final String[] args) {
		try {
			final String keySpace = "bb";
			final String columnFamily = "projectile_report";

			final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
			final StorageDaoUtil storageDaoUtil = injector.getInstance(StorageDaoUtil.class);
			final StorageIterator i = storageDaoUtil.keyIterator(keySpace, columnFamily);
			final StorageConfig config = injector.getInstance(StorageConfig.class);
			final String encoding = config.getEncoding();

			while (i.hasNext()) {
				final StorageValue key = i.next();
				final StorageValue name = storageDaoUtil.read(keySpace, columnFamily, key, new StorageValue("username", encoding));
				storageDaoUtil.insert(keySpace, columnFamily, key, new StorageValue("name", encoding), name);
			}
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
