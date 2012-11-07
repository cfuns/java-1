package de.benjaminborbe.storage.script;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.storage.util.StorageDaoUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class Script {

	public static void main(final String[] args) {
		final String keySpace = "bb";
		final String columnFamily = "bookmark";
		try {
			final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
			final StorageDaoUtil storageDaoUtil = injector.getInstance(StorageDaoUtil.class);
			final StorageIterator i = storageDaoUtil.keyIterator(keySpace, columnFamily);
			while (i.hasNext()) {
				return;
				// final byte[] key = i.nextByte();
				// try {
				// storageDaoUtil.delete(keySpace, columnFamily, key, "ownerUsername");
				// }
				// catch (final NotFoundException e) {
				// }
				// final String name = storageDaoUtil.read(keySpace, columnFamily, key,
				// "ownerUsername");
				// if (name != null) {
				// storageDaoUtil.insert(keySpace, columnFamily, key, "owner", name);
				// }
			}
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
