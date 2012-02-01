package de.benjaminborbe.monitoring.check;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashSet;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.guice.MonitoringModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NodeCheckerCacheTest {

	@Test
	public void testCache() {
		final Node node = EasyMock.createMock(Node.class);
		EasyMock.replay(node);
		final NodeCheckerImpl nodeChecker = EasyMock.createMock(NodeCheckerImpl.class);
		final Collection<CheckResult> checkResults = new HashSet<CheckResult>();
		EasyMock.expect(nodeChecker.checkNode(node)).andReturn(checkResults);
		EasyMock.replay(nodeChecker);
		final NodeCheckerCache nodeCheckerCache = new NodeCheckerCache(nodeChecker);

		// 1. call => nothing in cache
		{
			final Collection<CheckResult> result = nodeCheckerCache.checkNodeWithCache(node);
			assertNotNull(result);
			assertEquals(checkResults, result);
		}
		// 2. call => use cache
		{
			final Collection<CheckResult> result = nodeCheckerCache.checkNodeWithCache(node);
			assertNotNull(result);
			assertEquals(checkResults, result);
		}
	}

	@Test
	public void testCacheWithRootNode() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringModulesMock());
		final RootNode rootNodeA = injector.getInstance(RootNode.class);
		final RootNode rootNodeB = injector.getInstance(RootNode.class);
		final NodeCheckerImpl nodeChecker = EasyMock.createMock(NodeCheckerImpl.class);
		final Collection<CheckResult> checkResults = new HashSet<CheckResult>();
		EasyMock.expect(nodeChecker.checkNode(rootNodeA)).andReturn(checkResults);
		EasyMock.replay(nodeChecker);
		final NodeCheckerCache nodeCheckerCache = new NodeCheckerCache(nodeChecker);

		// 1. call => nothing in cache
		{
			final Collection<CheckResult> result = nodeCheckerCache.checkNodeWithCache(rootNodeA);
			assertNotNull(result);
			assertEquals(checkResults, result);
		}
		// 2. call => use cache
		{
			final Collection<CheckResult> result = nodeCheckerCache.checkNodeWithCache(rootNodeB);
			assertNotNull(result);
			assertEquals(checkResults, result);
		}
	}
}
