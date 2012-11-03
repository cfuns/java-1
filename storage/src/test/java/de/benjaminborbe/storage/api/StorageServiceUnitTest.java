package de.benjaminborbe.storage.api;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.storage.service.StorageServiceImpl;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.storage.util.StorageConnection;
import de.benjaminborbe.storage.util.StorageDaoUtil;
import de.benjaminborbe.storage.util.StorageKeyIterator;

public class StorageServiceUnitTest {

	@Test
	public void testfindByIdPrefix() throws Exception {
		final String keySpace = "keySpace";
		final String columnFamily = "columnFamily";
		final String prefix = "2011";
		final String encoding = "UTF-8";

		final StorageConnection storageConnection = EasyMock.createMock(StorageConnection.class);
		storageConnection.open();
		storageConnection.close();
		EasyMock.replay(storageConnection);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StorageConfig storageConfig = EasyMock.createMock(StorageConfig.class);
		EasyMock.expect(storageConfig.getKeySpace()).andReturn(keySpace).anyTimes();
		EasyMock.expect(storageConfig.getEncoding()).andReturn(encoding).anyTimes();
		EasyMock.replay(storageConfig);

		final StorageKeyIterator storageKeyIterator = EasyMock.createMock(StorageKeyIterator.class);
		EasyMock.expect(storageKeyIterator.hasNext()).andReturn(true);
		EasyMock.expect(storageKeyIterator.next()).andReturn("2011-01".getBytes(encoding));
		EasyMock.expect(storageKeyIterator.hasNext()).andReturn(true);
		EasyMock.expect(storageKeyIterator.next()).andReturn("2012-01".getBytes(encoding));
		EasyMock.expect(storageKeyIterator.hasNext()).andReturn(false);
		EasyMock.replay(storageKeyIterator);

		final StorageDaoUtil storageDaoUtil = EasyMock.createMock(StorageDaoUtil.class);
		EasyMock.expect(storageDaoUtil.keyIterator(keySpace, columnFamily)).andReturn(storageKeyIterator);
		EasyMock.replay(storageDaoUtil);

		final StorageService p = new StorageServiceImpl(logger, storageConfig, storageDaoUtil, storageConnection);

		// test call
		final Collection<String> result = p.findByIdPrefix(columnFamily, prefix);
		assertEquals(1, result.size());
	}
}
