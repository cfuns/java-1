package de.benjaminborbe.storage.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.locator.NetworkTopologyStrategy;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.ColumnDef;
import org.apache.cassandra.thrift.IndexType;
import org.apache.cassandra.thrift.KsDef;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageTestUtil {

	public static final String REPLICATION_FACTOR = "1";

	public static final String TYPE = "UTF8Type";

	public static final String FIELD_NAME = "indexedColumn";

	public static final String COLUMNFAMILY = "test";

	public static final String ENCODING = "UTF8";

	public static void setUp() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final Logger logger = injector.getInstance(Logger.class);
		final StorageConnectionPool connectionPool = injector.getInstance(StorageConnectionPool.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		StorageConnection connection = null;
		try {
			connection = connectionPool.getConnection();

			try {
				logger.debug("drop keyspace");
				connection.getClient().system_drop_keyspace(config.getKeySpace());
			}
			catch (final Exception e1) {
			}

			// Definition ders KeySpaces
			// Create CF
			final CfDef input = new CfDef(config.getKeySpace(), COLUMNFAMILY);
			input.setComparator_type(TYPE);
			input.setDefault_validation_class(TYPE);
			input.setKey_validation_class(TYPE);

			{
				final ColumnDef column = new ColumnDef();
				column.setName(FIELD_NAME.getBytes(ENCODING));
				column.setValidation_class(TYPE);
				// column.setIndex_name(FIELD_NAME + "_index");
				column.setIndex_type(IndexType.KEYS);
				input.addToColumn_metadata(column);
			}

			// Erstellt einen neuen KeySpace
			final List<CfDef> cfDefList = new ArrayList<CfDef>();
			cfDefList.add(input);
			final KsDef ksdef = new KsDef(config.getKeySpace(), NetworkTopologyStrategy.class.getName(), cfDefList);
			final Map<String, String> strategy_options = new HashMap<String, String>();
			// strategy_options.put("replication_factor", REPLICATION_FACTOR);
			strategy_options.put("datacenter1", "1");
			ksdef.setStrategy_options(strategy_options);
			ksdef.setDurable_writes(true);
			connection.getClient().system_add_keyspace(ksdef);

			final int magnitude = connection.getClient().describe_ring(config.getKeySpace()).size();
			try {
				Thread.sleep(1000 * magnitude);
			}
			catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}

		}
		catch (final Exception e) {
			logger.debug(e.getClass().getName(), e);
			try {
				logger.debug("drop keyspace");
				connection.getClient().system_drop_keyspace(config.getKeySpace());
			}
			catch (final Exception e1) {
			}
		}
		finally {
			connectionPool.close();
		}
	}

	public static void tearDown() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final Logger logger = injector.getInstance(Logger.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageConnectionPool connectionPool = injector.getInstance(StorageConnectionPool.class);

		StorageConnection connection = null;
		try {
			connection = connectionPool.getConnection();
			logger.debug("drop keyspace");
			connection.getClient().system_drop_keyspace(config.getKeySpace());
		}
		catch (final Exception e) {
		}

		finally {
			connectionPool.close();
		}
	}
}
