package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.easymock.EasyMock;
import org.junit.Test;

public class RandomUtilUnitTest {

	@Test
	public void testInt() throws Exception {
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(0)).andReturn(0);
			EasyMock.replay(random);
			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(100, randomUtil.getRandomized(100, 0));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(200)).andReturn(100);
			EasyMock.replay(random);
			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(100, randomUtil.getRandomized(100, 100));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(200)).andReturn(200);
			EasyMock.replay(random);
			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(200, randomUtil.getRandomized(100, 100));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(200)).andReturn(0);
			EasyMock.replay(random);
			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(0, randomUtil.getRandomized(100, 100));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(100)).andReturn(50);
			EasyMock.replay(random);
			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(100, randomUtil.getRandomized(100, 50));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(100)).andReturn(100);
			EasyMock.replay(random);
			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(150, randomUtil.getRandomized(100, 50));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(100)).andReturn(0);
			EasyMock.replay(random);
			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(50, randomUtil.getRandomized(100, 50));
		}
	}
}
