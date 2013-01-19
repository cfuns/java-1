package de.benjaminborbe.storage.script;

import java.util.Arrays;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.storage.util.StorageDaoUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class Script {

	public static void main(final String[] args) {
		try {
			System.out.println("started");
			final String keySpace = "bb";
			final String columnFamily = "task";

			final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
			final StorageDaoUtil storageDaoUtil = injector.getInstance(StorageDaoUtil.class);
			final StorageConfig config = injector.getInstance(StorageConfig.class);
			final String encoding = config.getEncoding();

			final StorageRowIterator i = storageDaoUtil.rowIterator(keySpace, columnFamily, Arrays.asList(new StorageValue("id", encoding), new StorageValue("focus", encoding)));
			while (i.hasNext()) {
				final StorageRow row = i.next();

				final StorageValue id = row.getValue(new StorageValue("id", encoding));
				final StorageValue focus = row.getValue(new StorageValue("focus", encoding));
				if (id != null && !id.isEmpty() && (focus == null || focus.isEmpty())) {
					storageDaoUtil.insert(keySpace, columnFamily, row.getKey(), new StorageValue("focus", encoding), new StorageValue("INBOX", encoding));
				}
			}
			System.out.println("finished");
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
