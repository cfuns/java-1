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
			final String columnFamily = "task";

			final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
			final StorageDaoUtil storageDaoUtil = injector.getInstance(StorageDaoUtil.class);
			final StorageIterator i = storageDaoUtil.keyIterator(keySpace, columnFamily);
			final StorageConfig config = injector.getInstance(StorageConfig.class);
			final String encoding = config.getEncoding();

			while (i.hasNext()) {
				final StorageValue key = i.next();
				final StorageValue completed = storageDaoUtil.read(keySpace, columnFamily, key, new StorageValue("completed", encoding));
				if ("true".equals(completed)) {
					// final String[] parts = datetime.split(" ");
					storageDaoUtil.insert(keySpace, columnFamily, key, new StorageValue("completionDate", encoding), new StorageValue(String.valueOf(System.currentTimeMillis()), encoding));
					System.err.println("insert");
				}

				// final byte[] key = i.nextByte();
				// try {
				// storageDaoUtil.delete(keySpace, columnFamily, key, "ownerUsername");
				// }
				// catch (final NotFoundException e) {
				// }
				//
			}
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
