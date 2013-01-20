package de.benjaminborbe.monitoring.check;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashSet;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NodeCheckerCacheIntegrationTest {

	@Test
	public void testCacheWithRootNode() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final RootNode rootNodeA = injector.getInstance(RootNode.class);
		final RootNode rootNodeB = injector.getInstance(RootNode.class);
		final NodeCheckerImpl nodeChecker = EasyMock.createMock(NodeCheckerImpl.class);
		final Collection<MonitoringCheckResult> checkResults = new HashSet<MonitoringCheckResult>();
		EasyMock.expect(nodeChecker.checkNode(rootNodeA)).andReturn(checkResults);
		EasyMock.replay(nodeChecker);
		final NodeCheckerCache nodeCheckerCache = new NodeCheckerCache(nodeChecker);

		// 1. call => nothing in cache
		{
			final Collection<MonitoringCheckResult> result = nodeCheckerCache.checkNodeWithCache(rootNodeA);
			assertNotNull(result);
			assertEquals(checkResults, result);
		}
		// 2. call => use cache
		{
			final Collection<MonitoringCheckResult> result = nodeCheckerCache.checkNodeWithCache(rootNodeB);
			assertNotNull(result);
			assertEquals(checkResults, result);
		}
	}
}
