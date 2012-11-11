package de.benjaminborbe.storage.script;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.guice.StorageModulesMock;
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
			while (i.hasNext()) {
				final byte[] key = i.nextByte();
				final String completed = storageDaoUtil.read(keySpace, columnFamily, key, "completed");
				if ("true".equals(completed)) {
					// final String[] parts = datetime.split(" ");
					storageDaoUtil.insert(keySpace, columnFamily, key, "completionDate", String.valueOf(System.currentTimeMillis()));
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
