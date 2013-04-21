package de.benjaminborbe.tools.util;

import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class RandomUtilUnitTest {

	@Test
	public void testRandomInt() throws Exception {
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(0)).andReturn(0);
			EasyMock.replay(random);

			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(0, randomUtil.getRandomInt(0, 0));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(1)).andReturn(0);
			EasyMock.replay(random);

			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(0, randomUtil.getRandomInt(0, 1));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(1)).andReturn(1);
			EasyMock.replay(random);

			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(1, randomUtil.getRandomInt(0, 1));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(1)).andReturn(0);
			EasyMock.replay(random);

			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(10, randomUtil.getRandomInt(10, 11));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(1)).andReturn(1);
			EasyMock.replay(random);

			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(11, randomUtil.getRandomInt(10, 11));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(100)).andReturn(0);
			EasyMock.replay(random);

			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(100, randomUtil.getRandomInt(100, 200));
		}
		{
			final Random random = EasyMock.createMock(Random.class);
			EasyMock.expect(random.nextInt(100)).andReturn(100);
			EasyMock.replay(random);

			final RandomUtil randomUtil = new RandomUtil(random);
			assertEquals(200, randomUtil.getRandomInt(100, 200));
		}
	}

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
