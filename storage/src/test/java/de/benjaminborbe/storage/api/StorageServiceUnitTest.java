package de.benjaminborbe.storage.api;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.storage.service.StorageServiceImpl;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.storage.util.StorageConnection;
import de.benjaminborbe.storage.util.StorageDaoUtil;

public class StorageServiceUnitTest {

	@Test
	public void testfindByIdPrefix() throws Exception {
		final String keySpace = "keySpace";
		final String columnFamily = "columnFamily";
		final String prefix = "2011";

		final StorageConnection storageConnection = EasyMock.createMock(StorageConnection.class);
		storageConnection.open();
		storageConnection.close();
		EasyMock.replay(storageConnection);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StorageConfig storageConfig = EasyMock.createMock(StorageConfig.class);
		EasyMock.expect(storageConfig.getKeySpace()).andReturn(keySpace);
		EasyMock.replay(storageConfig);

		final StorageDaoUtil storageDaoUtil = EasyMock.createMock(StorageDaoUtil.class);

		final List<String> ids = new ArrayList<String>();
		ids.add("2011-01");
		ids.add("2012-01");
		EasyMock.expect(storageDaoUtil.list(keySpace, columnFamily)).andReturn(ids);
		EasyMock.replay(storageDaoUtil);

		final StorageService p = new StorageServiceImpl(logger, storageConfig, storageDaoUtil, storageConnection);

		// test call
		final Collection<String> result = p.findByIdPrefix(columnFamily, prefix);
		assertEquals(1, result.size());
	}
}
